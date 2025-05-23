package com.example.plusproject.domain.book.service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.plusproject.domain.book.entity.Book;
import com.example.plusproject.domain.book.repository.BookRepository;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.enums.UserRole;
import com.example.plusproject.domain.user.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@Rollback // 테스트 종료 시 자동으로 DB 변경사항을 롤백해주는 어노테이션
class BookServiceTest {

	@Autowired
	private BookService bookService;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private UserRepository userRepository;

	@Mock
	private NaverBookSearchService naverBookSearchService;

	@BeforeEach
	void SetUp() {
		User user = new User(UserRole.valueOf("USER"), "test@example.com", "test12!", "testUser");
		userRepository.save(user);

		Book book = Book.builder()
			.stock(100)
			.build();
		ReflectionTestUtils.setField(book, "user", user);

		bookRepository.save(book);

		Book findBook = bookRepository.findByIdOrElseThrow(1L);
		System.out.println("초기 재고" + findBook.getStock());  // 초기 Stock 값 출력
	}


	@Test
	@DisplayName("낙관적 락을 이용한 동시성 제어")
	void concurrencyTestDecreaseStockWithOptimisticLock() {
		System.out.println("🔅🔅🔅🔅🔅🔅낙관적 락을 이용한 동시성 제어 시작🔅🔅🔅🔅🔅🔅");

		// AtomicInteger : 동시성 환경에서도 안전하게 값을 증가시킬 수 있는 Integer 클래스
		// 즉, 낙관적 락 실패한 횟수를 정확하게 세기 위해 사용한다
		// 여기서 0은 초기 값을 의미하고 73번 째 줄에 incrementAndGet 메서드로 1씩 증가시키며 카운트 하는 것!
		AtomicInteger optimisticLockFailures = new AtomicInteger(0);

		// 0부터 99까지 총 100개의 쓰레드가 동시에 decreaseStockWithOptimisticLock(1L) 을 호출하는 과정
		// .range(0, 100) : 0부터 99까지 총 100개의 숫자를 만드는 과정
		// .parallel() : 이 숫자들을 쓰레드에 분산하는 과정
		// .forEach() : 각각의 쓰레드가 1개씩 메서드를 호출!
		// 따라서 여러 쓰레드에서 병렬로 메서드가 호출되며 동시성 이슈가 발생한다
		IntStream.range(0, 100).parallel().forEach(i -> {
			try {
				bookService.decreaseStockWithOptimisticLock(1L);
			} catch (RuntimeException e) {
				System.out.println("💥💥💥💥💥💥낙관적 락으로 인한 데이터 변경 실패!💥💥💥💥💥💥");
				optimisticLockFailures.incrementAndGet();
			}
		});

		Book findBookAgain = bookRepository.findByIdOrElseThrow(1L);
		System.out.println("최종 재고" + findBookAgain.getStock()); // 최종 Stock 값 출력
		System.out.println("💦decreaseStock() 실패 횟수 :" + optimisticLockFailures.get());
	}

	@Test
	@DisplayName("낙관적 락을 이용한 동시성 제어 및 재시도 로직")
	void concurrencyTestDecreaseStockWithOptimisticLockAndRetry() {
		System.out.println("🔅🔅🔅🔅🔅🔅낙관적 락을 이용한 동시성 제어 시작🔅🔅🔅🔅🔅🔅");

		// 최대 재시도 횟수
		final int maxRetries = 1000;

		// 시도 횟수
		AtomicInteger totalAttempts = new AtomicInteger(0);

		// 성공 횟수
		AtomicInteger successfulDecrements = new AtomicInteger(0);

		IntStream.range(0, 100).parallel().forEach(i -> {
			boolean updated = false; // false면 업데이트 실패, true면 업데이트 성공
			int attempts = 0; // 시도 횟수

			while (!updated && attempts < maxRetries) {   // 성공하거나 최대 재시도 횟수를 초과하기 전까지 시도
				try {
					bookService.decreaseStockWithOptimisticLock(1L);
					successfulDecrements.incrementAndGet();
					updated = true;
				} catch (RuntimeException e) {
					attempts++;
				}
				totalAttempts.incrementAndGet(); // while문을 빠져나갈 때까지 모든 시도 횟수를 카운팅
			}

			// while 문에서 에서 실패한 경우 시도 횟수와 몇 번째 i에서 실패했는지 (0~99까지 중) 나타내는 것
			// 예를 들면 10개의 재고가 있고 100명의 유저가 있어서 100명의 유저가 동시에 10개의 재고에 접근한다면
			// 같은 version인 경우만 성공하므로 1개씩 성공이 되는데 나머지 99명은 재시도를 한다.
			// 근데 재시도끼리도 충돌이 나서 다시 재시도를 할 수도 있음!
			// 따라서 최종 재고가 남았음에도 이미 최대 재시도 횟수를 모두 소진하여 재시도를 못하게 된다
			if(!updated) {
				System.out.println("최대 재시도를 " + attempts + "회 하였으나 실패하였습니다. (for iteration " + i + "번째에서 발생.)");
			}
		});

		Book findBookAgain = bookRepository.findByIdOrElseThrow(1L);
		System.out.println("최종 재고" + findBookAgain.getStock()); // 최종 Stock 값 출력
		System.out.println("성공적으로 감소된 횟수: " + successfulDecrements.get());
		System.out.println("총 시도 횟수: " + totalAttempts.get());

		// 재고가 남았으니 이건 동시성 제어를 실패한 게 아닌가? 라고 생각했지만 이건 동시성 제어를 성공한것!
		// 왜냐하면 동시성 제어를 하는 목적은 재고는 10개인데 100명이 접근하여 재고 수보다 더 많은 갯수가 빠지게 되는 걸 방지하는 것이기 때문!
		// 즉, 접근 실패를 하게 하였으므로 동시성 제어를 성공한 것!
	}

}