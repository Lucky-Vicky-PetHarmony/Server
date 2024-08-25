package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.Word;
import luckyvicky.petharmony.repository.WordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;

@SpringBootTest
public class DetailAdoptionServiceTest {

    @Autowired
    private DetailAdoptionService detailAdoptionService;

    @MockBean
    private WordRepository wordRepository;

    @MockBean
    private PetInfoFormatService petInfoFormatService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    public void setUp() {
        // WordRepository의 동작을 Mock으로 설정
        Word word1 = new Word(1L, "건강한");
        Word word2 = new Word(2L, "회복중인");
        Word word3 = new Word(3L, "온순한");
        List<Word> wordList = Arrays.asList(word1, word2, word3);

        Mockito.when(wordRepository.findByWordIdIn(anyList())).thenReturn(wordList);
    }

    @Test
    public void testProcessPetInfo() throws ParseException {
        // Given
        PetInfo petInfo = new PetInfo();
        petInfo.setWords("1,2,3");
        petInfo.setColorCd("검정색");
        petInfo.setNoticeSdt(dateFormat.parse("2023-08-01"));
        petInfo.setNoticeEdt(dateFormat.parse("2023-08-15"));
        petInfo.setHappenDt(dateFormat.parse("2023-07-20"));
        petInfo.setHappenPlace("서울");
        petInfo.setCareNm("서울보호소");
        petInfo.setSpecialMark("특이사항 없음");

        Map<String, Object> mockBaseResult = new HashMap<>();
        mockBaseResult.put("kind_cd", "포메라니안");

        Mockito.when(petInfoFormatService.processPetInfo(petInfo)).thenReturn(mockBaseResult);

        // When
        Map<String, Object> result = detailAdoptionService.processPetInfo(petInfo);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get("words")).isEqualTo(Arrays.asList("건강한", "회복중인", "온순한"));
        assertThat(result.get("color_cd")).isEqualTo("검정색");
        assertThat(result.get("kind_cd")).isEqualTo("포메라니안");

        // 날짜를 문자열로 변환하여 비교
        assertThat(dateFormat.format(result.get("happen_dt"))).isEqualTo("2023-07-20");
        assertThat(result.get("notice_period")).isEqualTo("2023-08-01 ~ 2023-08-15");
        assertThat(result.get("happen_place")).isEqualTo("서울");
        assertThat(result.get("care_nm")).isEqualTo("서울보호소");
        assertThat(result.get("special_mark")).isEqualTo("특이사항 없음");
    }

    @Test
    public void testProcessSpecies() {
        // Given
        String species1 = "[고양이] 페르시안";
        String species2 = "기타축종";

        // When
        String result1 = detailAdoptionService.processSpecies(species1);
        String result2 = detailAdoptionService.processSpecies(species2);

        // Then
        assertThat(result1).isEqualTo(" 페르시안");
        assertThat(result2).isEqualTo("-");
    }
}
