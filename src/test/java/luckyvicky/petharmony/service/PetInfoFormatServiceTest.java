package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.ShelterInfo;
import luckyvicky.petharmony.entity.Word;
import luckyvicky.petharmony.repository.ShelterInfoRepository;
import luckyvicky.petharmony.repository.WordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PetInfoFormatServiceTest {

    private PetInfoFormatService petInfoFormatService;
    private WordRepository wordRepository;
    private ShelterInfoRepository shelterInfoRepository;

    @BeforeEach
    public void setUp() {
        // Given: Mock WordRepository and ShelterInfoRepository is set up with expected data
        wordRepository = Mockito.mock(WordRepository.class);
        shelterInfoRepository = Mockito.mock(ShelterInfoRepository.class);

        // 테스트 데이터 기반의 Word ID 매핑 설정
        List<Word> words = Arrays.asList(
                new Word(1L, "건강한"),
                new Word(3L, "온순한"),
                new Word(7L, "겁많은"),
                new Word(17L, "특별한")
        );
        Mockito.when(wordRepository.findByWordIdIn(Arrays.asList(1L, 3L, 7L, 17L)))
                .thenReturn(words);

        // 테스트 데이터 기반의 ShelterInfo 매핑 설정
        ShelterInfo shelterInfo = new ShelterInfo();
        shelterInfo.setCareNm("한국동물구조관리협회");
        shelterInfo.setOrgNm("서울특별시");

        Mockito.when(shelterInfoRepository.findByCareNm("한국동물구조관리협회"))
                .thenReturn(Optional.of(shelterInfo));

        // Mock된 WordRepository와 ShelterInfoRepository로 PetInfoFormatService를 초기화합니다.
        petInfoFormatService = new PetInfoFormatService(wordRepository, shelterInfoRepository);
    }

    @Test
    public void testProcessPetInfo() {
        // Given: A PetInfo object is created with specified attributes
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

        // When: PetInfoFormatService's processPetInfo method is called
        Map<String, Object> result = petInfoFormatService.processPetInfo(petInfo);

        // Then: The result should match the expected processed values
        System.out.println("결과: " + result);

        // 각 필드의 올바른 처리 확인을 위한 Assertions
        assertEquals(Arrays.asList("건강한", "온순한", "겁많은", "특별한"), result.get("words"));
        assertEquals("포메라니안", result.get("kind_cd"));
        assertEquals("2017년생", result.get("age"));
        assertEquals("남아", result.get("sex_cd"));
        assertEquals("중성화 완료", result.get("neuter_yn"));
        assertEquals("서울특별시", result.get("care_nm"));
        assertEquals("http://www.animal.go.kr/files/shelter/2024/07/20240731150770.jpg", result.get("popfile")); // popfile 필드 검증
    }
}
