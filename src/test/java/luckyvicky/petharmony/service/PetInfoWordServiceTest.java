package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.WordClassificationDTO;
import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.repository.PetInfoRepository;
import luckyvicky.petharmony.service.openapi.OpenAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PetInfoWordServiceTest {

    @Mock
    private PetInfoRepository petInfoRepository;

    @Mock
    private OpenAiService openAiService;

    @InjectMocks
    private PetInfoWordService petInfoWordService;

    // Mock 초기화
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * processPetInfo 메서드가 desertionNo가 null인 경우를 처리하는지 테스트
     */
    @Test
    void testProcessPetInfo_withNullDesertionNo() {
        // given: desertionNo가 null인 PetInfo 객체를 생성
        PetInfo petInfo = new PetInfo();
        petInfo.setDesertionNo(null);

        // when: processPetInfo 메서드를 호출
        petInfoWordService.processPetInfo(petInfo);

        // then
        verify(petInfoRepository, never()).save(any(PetInfo.class));
        verify(openAiService, never()).analyzeSpecialMark(anyString());
    }

    /**
     * processPetInfo 메서드가 이미 words 필드에 데이터가 있는 경우 OpenAI 호출을 건너뛰는지 테스트
     */
    @Test
    void testProcessPetInfo_withExistingWords() {
        // given: words 필드가 이미 채워진 PetInfo 객체를 생성
        PetInfo petInfo = new PetInfo();
        petInfo.setDesertionNo("12345");
        petInfo.setWords("이미 존재하는 단어");

        // when: processPetInfo 메서드를 호출
        petInfoWordService.processPetInfo(petInfo);

        // then
        verify(petInfoRepository, never()).save(any(PetInfo.class));
        verify(openAiService, never()).analyzeSpecialMark(anyString());
    }

    /**
     * processPetInfo 메서드가 OpenAI API를 호출하고 words 필드를 업데이트하는지 테스트
     */
    @Test
    void testProcessPetInfo_updatesWords() {
        // given: 분석이 필요한 PetInfo 객체를 생성
        PetInfo petInfo = new PetInfo();
        petInfo.setDesertionNo("12345");
        petInfo.setSpecialMark("특별한 특성");

        // Mock OpenAI 분석 결과
        when(openAiService.analyzeSpecialMark("특별한 특성")).thenReturn("건강한, 회복중인");

        // when: processPetInfo 메서드를 호출하여 OpenAI API를 통해 분석을 수행
        petInfoWordService.processPetInfo(petInfo);

        // then
        // PetInfo가 저장될 때 사용된 객체를 캡처
        ArgumentCaptor<PetInfo> petInfoCaptor = ArgumentCaptor.forClass(PetInfo.class);
        verify(petInfoRepository).save(petInfoCaptor.capture());

        assertEquals("1,2", petInfoCaptor.getValue().getWords()); // "건강한"과 "회복중인"이 매칭된 "1,2"로 설정되었는지 확인
    }

    /**
     * processPetInfo 메서드가 specialMark 필드가 비어있을 때의 동작을 테스트합니다.
     */
    @Test
    void testProcessPetInfo_withEmptySpecialMark() {
        // given: specialMark 필드가 null인 PetInfo 객체를 생성
        PetInfo petInfo = new PetInfo();
        petInfo.setDesertionNo("12345");
        petInfo.setSpecialMark(null);

        // when: processPetInfo 메서드를 호출
        petInfoWordService.processPetInfo(petInfo);

        // then
        verify(openAiService, never()).analyzeSpecialMark(anyString());
        verify(petInfoRepository).save(petInfo); // 빈 words 상태로 저장은 진행되는지 확인
    }
}
