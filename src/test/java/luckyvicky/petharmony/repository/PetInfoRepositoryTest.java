package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.dto.WordClassificationDTO;
import luckyvicky.petharmony.entity.PetInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Date;
import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // MySQL 사용하도록 설정
public class PetInfoRepositoryTest {

    @Autowired
    private PetInfoRepository petInfoRepository;

    @BeforeEach
    void setUp() {
        // 테스트 데이터베이스를 초기화하기 위한 샘플 데이터 삽입
        PetInfo petInfo1 = PetInfo.builder()
                .desertionNo("1")
                .happenDt(createDate(2023, Calendar.AUGUST, 20))  // 유기된 날짜
                .happenPlace("Park")  // 유기된 장소
                .kindCd("Dog")  // 동물 종류
                .colorCd("Black")  // 동물 색상
                .age("2")  // 동물 나이
                .weight("15kg")  // 동물 무게
                .noticeNo("N001")  // 공고번호
                .noticeSdt(createDate(2023, Calendar.AUGUST, 21))  // 공고 시작일
                .noticeEdt(createDate(2023, Calendar.AUGUST, 31))  // 공고 종료일
                .popfile("img1.jpg")  // 이미지 파일 경로
                .processState("Processing")  // 처리 상태
                .sexCd("M")  // 성별 코드
                .neuterYn("Y")  // 중성화 여부
                .specialMark("Friendly")  // 특이사항
                .careNm("Care Center 1")  // 보호소 이름
                .words("word1")  // 연관된 단어
                .build();

        PetInfo petInfo2 = PetInfo.builder()
                .desertionNo("2")
                .happenDt(createDate(2023, Calendar.AUGUST, 22))
                .happenPlace("Shelter")
                .kindCd("Cat")
                .colorCd("White")
                .age("3")
                .weight("5kg")
                .noticeNo("N002")
                .noticeSdt(createDate(2023, Calendar.AUGUST, 23))
                .noticeEdt(createDate(2023, Calendar.SEPTEMBER, 2))
                .popfile("img2.jpg")
                .processState("Adopted")
                .sexCd("F")
                .neuterYn("N")
                .specialMark("Playful")
                .careNm("Care Center 2")
                .words("word2")
                .build();

        // 데이터베이스에 샘플 데이터 저장
        petInfoRepository.save(petInfo1);
        petInfoRepository.save(petInfo2);
    }

    /**
     * 유틸리티 메서드: 주어진 연도, 월, 일에 해당하는 Date 객체를 생성하여 반환
     *
     * @param year  연도
     * @param month 월 (0부터 시작)
     * @param day   일
     * @return 주어진 날짜에 해당하는 Date 객체
     */
    private Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    @Test
    void testFindWordClassificationsByCriteria() {
        // Given: 페이징 설정 (페이지 0, 사이즈 10)
        Pageable pageable = PageRequest.of(0, 10);

        // When: age가 "2"이고 sexCd가 "M"인 PetInfo를 검색
        Page<WordClassificationDTO> result = petInfoRepository.findWordClassificationsByCriteria("2", "M", pageable);

        // Then: 결과가 null이 아니고, 한 개의 결과만 반환되는지 확인
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);

        // 반환된 결과가 예상한 desertionNo, age, sexCd와 일치하는지 확인
        assertThat(result.getContent().get(0).getDesertionNo()).isEqualTo("1");
        assertThat(result.getContent().get(0).getAge()).isEqualTo("2");
        assertThat(result.getContent().get(0).getSexCd()).isEqualTo("M");
    }

    @Test
    void testFindByWordId() {
        // When: 단어 ID가 "word1"인 PetInfo를 검색
        List<PetInfo> result = petInfoRepository.findByWordId("word1");

        // Then: 결과가 null이 아니고, 한 개의 결과만 반환되는지 확인
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);

        // 반환된 결과의 desertionNo가 예상한 값과 일치하는지 확인
        assertThat(result.get(0).getDesertionNo()).isEqualTo("1");
    }
}
