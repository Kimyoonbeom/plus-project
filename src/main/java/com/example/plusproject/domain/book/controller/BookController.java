package com.example.plusproject.domain.book.controller;

import com.example.plusproject.common.dto.AuthUser;
import com.example.plusproject.domain.book.dto.BookRequestDto;
import com.example.plusproject.domain.book.dto.BookResponseDto;
import com.example.plusproject.domain.book.service.AladinBookImportService;
import com.example.plusproject.domain.book.service.BookService;
import com.example.plusproject.domain.book.service.NaverBookSearchService;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AladinBookImportService aladinBookImportService;

    private final NaverBookSearchService naverBookSearchService;

    private final UserRepository userRepository;


    // 도서 등록
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody BookRequestDto dto,
                                       @AuthenticationPrincipal AuthUser authUser) {
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return ResponseEntity.ok(bookService.createBook(dto, user));
    }

     // 도서 단건 조회
    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponseDto> get(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBook(bookId));
    }

    // 도서 수정
    @PutMapping("/{bookId}")
    public ResponseEntity<Void> update(@PathVariable Long bookId,
                                       @RequestBody BookRequestDto dto,
                                       @AuthenticationPrincipal AuthUser authUser) {
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        bookService.updateBook(bookId, dto, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> delete(@PathVariable Long bookId,
                                       @AuthenticationPrincipal AuthUser authUser) {
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        bookService.deleteBook(bookId, user);
        return ResponseEntity.ok().build();
    }

    // 도서 검색
    @GetMapping("/search")
    public ResponseEntity<List<BookResponseDto>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(bookService.searchBooks(keyword));
    }

    @PostMapping("/savebookfromaladin")
    public ResponseEntity<String> saveBooksFromAladin(){
        // List<String> keywords = List.of(
        //     "자바","한강","파이썬", "리액트", "AI", "알고리즘", "컴퓨터", "웹", "백엔드", "프론트엔드",
        //     "데이터베이스", "딥러닝", "머신러닝", "코틀린", "스프링", "HTML", "CSS", "JavaScript", "Node", "Docker",
        //     "쿠버네티스", "DevOps", "클린코드", "JPA", "SQL", "데이터분석", "프로그래밍", "C언어", "C++", "운영체제",
        //     "리눅스", "네트워크", "정보보안", "인공지능", "디자인패턴", "토익", "토플", "일본어", "중국어"
        // );
        // List<String> keywords = List.of("영어회화","자기계발", "리더십", "경제", "경영", "마케팅", "브랜딩",
        //     "심리학", "역사", "소설", "에세이","여행","투자","철학","정치","주식","취미","사랑","평화","믿음","미국","한국");
        // List<String> keywords = List.of("곤충 공부법", "식물 전략", "재판 전략", "환경 입문", "사건의 이론", "생물학 가이드", "공부 활용법", "노동 입문", "동물 완전정복", "학습 개론", "결혼 핵심", "과외 활용법", "헬스 완전정복", "수능 완전정복", "법률 완전정복", "복지 개론", "복지 활용법", "과외 전략", "노동의 이론", "자소서 활용법", "도교의 이론", "승진 공부법", "천주교 활용법", "이력서 핵심", "학습 핵심", "법률의 기초", "수능 공부법", "재판 핵심", "식물 가이드", "건강 입문", "출산 가이드", "요리 핵심", "조직문화의 기초", "디지털 핵심", "등산의 이론", "가족 활용법", "종교 활용법", "자연 개론", "시험 전략", "면접 기술", "글쓰기 전략", "자기소개서", "논술 준비", "자격증 공부법", "직장 예절", "프레젠테이션", "자료조사법", "사회 트렌드", "시사 이해", "전기 공학", "기계 기초", "간호학 개론");
        List<String> keywords = List.of(
            "한국사", "세계사", "미술사", "건축학", "디자인", "미학", "예술", "문예", "현대사", "철학사",
            "물리학", "화학", "생물학", "천문학", "지구과학", "우주과학", "환경과학", "기상학", "해양학", "통계학",
            "심리테스트", "성격유형", "MBTI", "상담심리", "청소년심리", "노인심리", "범죄심리", "인지심리", "행동심리", "심리상담",
            "헌법", "형법", "민법", "행정법", "형사소송법", "민사소송법", "노동법", "국제법", "지식재산권", "사법시험",
            "회계원리", "재무회계", "관리회계", "세무회계", "회계사", "세무사", "감사론", "재무관리", "회계감사", "경영분석",
            "마케팅전략", "고객관리", "브랜드마케팅", "SNS마케팅", "퍼포먼스마케팅", "광고전략", "세일즈", "시장조사", "PR", "콘텐츠마케팅",
            "논문작성법", "보고서작성", "자기소개서작성", "독서감상문", "논리적글쓰기", "토론기술", "설득기술", "인터뷰기술", "브리핑", "말하기기술",
            "면접준비", "공무원면접", "기업면접", "면접후기", "면접기출", "NCS", "직무기술서", "이력서작성", "자소서첨삭", "입사지원전략",
            "스마트폰활용", "엑셀활용", "파워포인트", "포토샵", "일러스트", "프리미어", "영상편집", "코딩입문", "웹디자인", "UIUX디자인",
            "자연과학", "사회과학", "인문학", "언어학", "문헌정보학", "교육학", "교육심리", "유아교육", "특수교육", "교육철학",
            "사서직", "행정직", "기술직", "보건직", "소방직", "경찰직", "검찰직", "세무직", "통계직", "교육행정",
            "주식투자", "부동산", "재테크", "금융이론", "비트코인", "ETF", "펀드", "부자되는법", "자산관리", "경제신문읽기",
            "일기예보", "기후위기", "탄소중립", "재생에너지", "에너지정책", "핵발전", "풍력발전", "태양광", "기후과학", "환경운동",
            "AI윤리", "기술철학", "미래사회", "디지털전환", "4차산업혁명", "메타버스", "블록체인", "웹3.0", "NFT", "가상현실",
            "캠핑", "등산", "낚시", "여행기", "도보여행", "세계여행", "국내여행", "자전거", "바다여행", "차박",
            "그림책", "아동문학", "청소년소설", "고전문학", "현대문학", "세계문학", "단편소설", "산문집", "에세이집", "작가론",
            "요가", "필라테스", "홈트레이닝", "헬스", "보디빌딩", "다이어트", "영양학", "건강식단", "스트레칭", "생활체육",
            "악기", "기타", "피아노", "드럼", "바이올린", "색소폰", "우쿨렐레", "플루트", "작곡", "편곡",
            "연극", "영화", "뮤지컬", "다큐멘터리", "드라마", "연기", "영화제작", "시나리오", "영화비평", "연극이론"
        );
        aladinBookImportService.importBooksFromAladin(keywords);
        return ResponseEntity.ok("저장 완료");
    }

    @PostMapping("/naver")
    public ResponseEntity<String> saveBooksFromNaver(){
        /*List<String> keywords = List.of("자바","한강","파이썬", "리액트", "AI", "알고리즘", "컴퓨터", "웹", "백엔드", "프론트엔드",
            "데이터베이스", "딥러닝", "머신러닝", "코틀린", "스프링", "HTML", "CSS", "JavaScript", "Node", "Docker",
            "쿠버네티스", "DevOps", "클린코드", "JPA", "SQL", "데이터분석", "프로그래밍", "C언어", "C++", "운영체제",
            "리눅스", "네트워크", "정보보안", "인공지능", "디자인패턴", "토익", "토플", "일본어", "중국어","영어회화","자기계발",
            "리더십", "경제", "경영", "마케팅", "브랜딩","심리학", "역사", "소설", "에세이","여행","투자","철학","정치","주식",
            "취미","사랑","평화","믿음","미국","한국"
        );*/
        List<String> keywords = List.of(
            "한국사", "세계사", "미술사", "건축학", "디자인", "미학", "예술", "문예", "현대사", "철학사",
            "물리학", "화학", "생물학", "천문학", "지구과학", "우주과학", "환경과학", "기상학", "해양학", "통계학",
            "심리테스트", "성격유형", "MBTI", "상담심리", "청소년심리", "노인심리", "범죄심리", "인지심리", "행동심리", "심리상담",
            "헌법", "형법", "민법", "행정법", "형사소송법", "민사소송법", "노동법", "국제법", "지식재산권", "사법시험",
            "회계원리", "재무회계", "관리회계", "세무회계", "회계사", "세무사", "감사론", "재무관리", "회계감사", "경영분석",
            "마케팅전략", "고객관리", "브랜드마케팅", "SNS마케팅", "퍼포먼스마케팅", "광고전략", "세일즈", "시장조사", "PR", "콘텐츠마케팅"//,
            // "논문작성법", "보고서작성", "자기소개서작성", "독서감상문", "논리적글쓰기", "토론기술", "설득기술", "인터뷰기술", "브리핑", "말하기기술",
            // "면접준비", "공무원면접", "기업면접", "면접후기", "면접기출", "NCS", "직무기술서", "이력서작성", "자소서첨삭", "입사지원전략",
            // "스마트폰활용", "엑셀활용", "파워포인트", "포토샵", "일러스트", "프리미어", "영상편집", "코딩입문", "웹디자인", "UIUX디자인",
            // "자연과학", "사회과학", "인문학", "언어학", "문헌정보학", "교육학", "교육심리", "유아교육", "특수교육", "교육철학",
            // "사서직", "행정직", "기술직", "보건직", "소방직", "경찰직", "검찰직", "세무직", "통계직", "교육행정",
            // "주식투자", "부동산", "재테크", "금융이론", "비트코인", "ETF", "펀드", "부자되는법", "자산관리", "경제신문읽기",
            // "일기예보", "기후위기", "탄소중립", "재생에너지", "에너지정책", "핵발전", "풍력발전", "태양광", "기후과학", "환경운동",
            // "AI윤리", "기술철학", "미래사회", "디지털전환", "4차산업혁명", "메타버스", "블록체인", "웹3.0", "NFT", "가상현실",
            // "캠핑", "등산", "낚시", "여행기", "도보여행", "세계여행", "국내여행", "자전거", "바다여행", "차박",
            // "그림책", "아동문학", "청소년소설", "고전문학", "현대문학", "세계문학", "단편소설", "산문집", "에세이집", "작가론",
            // "요가", "필라테스", "홈트레이닝", "헬스", "보디빌딩", "다이어트", "영양학", "건강식단", "스트레칭", "생활체육",
            // "악기", "기타", "피아노", "드럼", "바이올린", "색소폰", "우쿨렐레", "플루트", "작곡", "편곡",
            // "연극", "영화", "뮤지컬", "다큐멘터리", "드라마", "연기", "영화제작", "시나리오", "영화비평", "연극이론"
        );
        naverBookSearchService.importBooksFromNaver(keywords);
        return ResponseEntity.ok("저장 완료");
    }
}
