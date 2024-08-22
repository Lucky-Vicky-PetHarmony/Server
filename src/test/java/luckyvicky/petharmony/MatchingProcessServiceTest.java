package luckyvicky.petharmony;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.Word;
import luckyvicky.petharmony.repository.WordRepository;
import luckyvicky.petharmony.service.MatchingProcessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatchingProcessServiceTest {

    private MatchingProcessService matchingProcessService;

    @BeforeEach
    public void setUp() {
        // Given: Mock WordRepository is set up with expected word mappings
        // WordRepository를 Mocking하여 예상되는 단어 매핑을 설정합니다.
        WordRepository wordRepository = Mockito.mock(WordRepository.class);

        // 테스트 데이터 기반의 Word ID 매핑 설정
        List<Word> words = Arrays.asList(
                new Word(1L, "건강한"),
                new Word(3L, "온순한"),
                new Word(7L, "겁많은"),
                new Word(17L, "특별한")
        );
        Mockito.when(wordRepository.findByWordIdIn(Arrays.asList(1L, 3L, 7L, 17L)))
                .thenReturn(words);

        // Mock된 WordRepository로 MatchingProcessService를 초기화합니다.
        matchingProcessService = new MatchingProcessService(wordRepository);
    }

    @Test
    public void testProcessPetInfo() {
        // Given: A PetInfo object is created with specified attributes
        // 지정된 속성들로 PetInfo 객체를 생성합니다.
        PetInfo petInfo = new PetInfo();
        petInfo.setDesertionNo("411300202400334");
        petInfo.setHappenPlace("종로 386 진형빌딩 인근");
        petInfo.setKindCd("[개] 포메라니안");
        petInfo.setColorCd("검");
        petInfo.setAge("2017(년생)");
        petInfo.setWeight("4(Kg)");
        petInfo.setNoticeNo("서울-종로-2024-00128");
        petInfo.setPopfile("http://www.animal.go.kr/files/shelter/2024/07/20240731150770.jpg");
        petInfo.setProcessState("보호중");
        petInfo.setSexCd("M");
        petInfo.setNeuterYn("Y");
        petInfo.setSpecialMark("온순. 소심. 치석. 양쪽눈 혼탁. 코검정. 꼬리단미안됨. 털상태양호. 오른쪽슬개골.");
        petInfo.setCareNm("한국동물구조관리협회");
        petInfo.setChargeNm("김민지");
        petInfo.setOfficetel("02-2148-1894");
        petInfo.setWords("1,3,7,17");

        // When: MatchingProcessService's processPetInfo method is called
        // MatchingProcessService의 processPetInfo 메서드를 호출합니다.
        Map<String, Object> result = matchingProcessService.processPetInfo(petInfo);

        // Then: The result should match the expected processed values
        // 결과가 예상된 처리된 값과 일치해야 합니다.
        System.out.println("결과: " + result);

        // 각 필드의 올바른 처리 확인을 위한 Assertions
        assertEquals(Arrays.asList("건강한", "온순한", "겁많은", "특별한"), result.get("words"));
        assertEquals("포메라니안", result.get("kind_cd"));
        assertEquals("2017년생", result.get("age"));
        assertEquals("남아", result.get("sex_cd"));
        assertEquals("중성화 완료", result.get("neuter_yn"));
    }
}

