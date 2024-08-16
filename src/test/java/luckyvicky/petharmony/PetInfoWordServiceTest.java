package luckyvicky.petharmony;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.repository.PetInfoRepository;
import luckyvicky.petharmony.service.PetInfoWordService;
import luckyvicky.petharmony.service.openapi.OpenAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PetInfoWordServiceTest {

    @Mock
    private PetInfoRepository petInfoRepository;

    @Mock
    private OpenAiService openAiService;

    @InjectMocks
    private PetInfoWordService petInfoWordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessPetInfoWithAgeAndSex() {
        // Given: PetInfo 객체 생성 및 초기화
        PetInfo petInfo = new PetInfo();
        petInfo.setDesertionNo("311300202400322");
        petInfo.setSpecialMark("특별한 특징이 있습니다.");
        petInfo.setAge("2024(년생)");
        petInfo.setSexCd("M");

        // Mock 설정: OpenAiService의 analyzeSpecialMark 메서드가 특정 결과를 반환하도록 설정
        when(openAiService.analyzeSpecialMark("특별한 특징이 있습니다.")).thenReturn("특별한");

        // When: processPetInfo 메서드 호출
        petInfoWordService.processPetInfo(petInfo);

        // Then: words 필드가 예상한 대로 저장되었는지 확인
        // 2024년생이므로 "활발한", 성별이 "M"이므로 "예쁜", OpenAi 분석 결과로 "특별한"이 추가되어야 함
        String expectedWords = "5,11,17";
        assertEquals(expectedWords, petInfo.getWords());

        // 저장 메서드 호출 확인
        verify(petInfoRepository, times(1)).save(petInfo);

        // 결과 출력
        System.out.println("저장된 words 필드 값: " + petInfo.getWords());
    }
}
