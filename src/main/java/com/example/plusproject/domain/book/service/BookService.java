package com.example.plusproject.domain.book.service;

import com.example.plusproject.domain.book.dto.BookRequestDto;
import com.example.plusproject.domain.book.dto.BookResponseDto;
import com.example.plusproject.domain.book.entity.Book;
import com.example.plusproject.domain.book.repository.BookRepository;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.enums.UserRole;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final RedissonClient redissonClient;
    private final RedisTemplate<String, Long> redisTemplate;

    // Redisson 을 하기 위한 키 설정
    private static final String LOCK_KEY = "stockLock";

    public Long createBook(BookRequestDto dto, User user) {
        Book book = Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .publisher(dto.getPublisher())
                .description(dto.getDescription())
                .publishedAt(dto.getPublishedAt())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .rating(null)  // 최초에는 null
                .build();

        return bookRepository.save(book).getId();
    }

    public BookResponseDto getBook(Long bookId) {
        return new BookResponseDto(getBookEntity(bookId));
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "bookSearchLocal", allEntries = true, cacheManager = "localCacheManager"),
        @CacheEvict(value = "bookSearchRedis", allEntries = true, cacheManager = "redisCacheManager"),
    })
    public void updateBook(Long bookId, BookRequestDto dto, User user) {
        Book book = getBookEntity(bookId);

        if(!user.getUserRole().equals(UserRole.ADMIN)){
            throw new SecurityException("수정 권한이 없습니다.");
        }

        book.update(dto);
    }

    @Caching(evict = {
        @CacheEvict(value = "bookSearchLocal", allEntries = true, cacheManager = "localCacheManager"),
        @CacheEvict(value = "bookSearchRedis", allEntries = true, cacheManager = "redisCacheManager"),
    })
    public void deleteBook(Long bookId, User user) {
        Book book = getBookEntity(bookId);

        if(!user.getUserRole().equals(UserRole.ADMIN)){
            throw new SecurityException("수정 권한이 없습니다.");
        }

        bookRepository.delete(book);
    }

    public List<BookResponseDto> searchBooks(String keyword) {
        return bookRepository.findByTitleContainingOrAuthorContaining(keyword, keyword)
                .stream()
                .map(BookResponseDto::new)
                .collect(Collectors.toList());
    }


    private Book getBookEntity(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("책이 존재하지 않습니다."));
    }

    /**
     * 재고 감소 로직
     * 동시성 제어 - 낙관적 락 방법
     * 최대 재시도 횟수 1000번으로 설정
     * @param id
     */
    @Transactional
    public void decreaseStockWithOptimisticLock(Long id) {

        int maxRetries = 1000;  // 최대 재시도 횟수
        int attempts = 0;   // 시도 횟수
        boolean updated = false;    // false면 업데이트 실패, true면 업데이트 성공

        while(!updated && attempts < maxRetries) {
            try {
                decreaseStock(id);  // 트랜잭션 관리를 위해 메서드 분리
                updated = true;
            } catch (RuntimeException e) {
                attempts++;
            }
        }

        if(!updated) {
            throw new RuntimeException("최대 재시도 횟수만큼 시도하였으나 실패하였습니다");
        }
    }

    /**
     * 재고 감소 로직
     * 이 부분을 따로 빼주는 이유는 트랜잭션 관리를 위함
     * @param id
     */
    @Transactional
    public void decreaseStock(Long id) {
        Book findbook = bookRepository.findByIdOrElseThrow(id);
        findbook.decreaseStock();
        //💥어차피 JPA가 Version 관리를 통해 영속성 컨텍스트 내에 있으므로 굳이 save() 안해줘도 Transactional 종료 시에 자동으로 flush 된다!
        // bookRepository.save(findbook);
    }

    /**
     * 재고 감소 로직
     * 동시성 제어 - Redisson 방법
     * @param id
     */
    public void decreaseStockWithRedisson(Long id) {
        // 34번 째 줄에 선언한 LOCK_KEY 기준으로 공정 락(FairLock) 생성
        // 이 때에 Redis에 연결됨! (RedisConfig에 설정되어 있다!)
        // 공정락은 먼저 락 요청한 스레드에 먼저 락을 줘서 공평하게 처리해준다
        // 일반 락은 순서 고려 안하고 운빨로 락을 얻는다
        // LOCK_KEY를 바로 입력 안해주고 굳이 필드 선언하는 이유는
        // 1. 가독성 향상 - 락에 사용할 고정 키라는 것을 명시해줌
        // 2. 재사용성 - 여러 곳에서 LOCK_KEY를 사용하는 경우 재사용 가능
        // 3. 정책 관리 용이 - 나중에 LOCK_KEY 네이밍 전략이 바뀌는 경우 변수 하나만 고치면 됨!
        // 즉, 이렇게 해주는게 좋은 코드 습관이자 클린 코드 전략이 된다!
        RLock fairLock = redissonClient.getFairLock(LOCK_KEY);

        try {
            // 최대 10초까지 기다렸다가 락을 얻으면 true 리턴
            // 만약 락을 얻었다면 60초간 점유 가능 (즉, 자동 만료시간이 설정된 것)
            boolean isLocked = fairLock.tryLock(10, 60, TimeUnit.SECONDS);

            // 락을 성공적으로 얻었을 경우 재고 감소 로직 실행
            if (isLocked) {
                Book findbook = bookRepository.findByIdOrElseThrow(id);
                findbook.decreaseStock();
                bookRepository.save(findbook);
            }

            // 락을 기다리는 중 스레드가 인터럽트 될 경우 그 신호를 복원해주는 것
            // 엥? Redis는 싱글 쓰레드인데 왜 인터럽트 당하지?
            // 여기서 InterruptedException이 발생하는 이유는 Redis 때문이 아니라
            // try 안에서 설정한 tryLock()이 쓰레드 대기 중에 다른 쓰레드나 OS에서 강제로 깨우는 경우 발생!(Java는 멀티 쓰레드이기 때문)
            // 즉, 동시에 여러 요청이 들어와도 Redis는 싱글 쓰레드이기 때문에 1개만 처리된다.
            // 나머지 요청들은 위에서 설정한 tryLock의 규칙대로 10초동안 대기상태에 빠지는데 그 때 interrupt 될 경우 그 신호를 복구해준다
            // 인터럽트는 스레드 종료 요청 신호로써 자동으로 발생하지 않고 다른 스레드가 interrupt()를 호출해야 발생한다
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            // 모든 걸 진행한 후에 락 해제를 해주는 과정
            // 이걸 안해주면 다음 진입하는 사용자들은 락 대기만 하다가 뻗을 수 있다
            // (위에 설정한 10초의 시간이 지나면 뻗음)
        } finally {
            if (fairLock.isHeldByCurrentThread()) {
                fairLock.unlock();
            }
        }
    }
}
