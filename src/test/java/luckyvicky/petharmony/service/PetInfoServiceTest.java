package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.repository.PetInfoRepository;
import luckyvicky.petharmony.repository.UserWordRepository;
import luckyvicky.petharmony.service.WordMatchingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

public class PetInfoServiceTest {

    @Mock
    private PetInfoRepository petInfoRepository;

    @Mock
    private UserWordRepository userWordRepository;

    @InjectMocks
    private WordMatchingService wordMatchingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mock 객체 초기화
    }

    @Test
    public void testGetTop12PetInfosByUserWord() {
        // Mock 데이터 준비
        Long userId = 27L;
        List<Long> mockWordIds = Arrays.asList(1L, 3L, 5L, 20L); // 사용자가 선택한 단어 ID 리스트

        // PetInfo 객체를 Mock 데이터로 생성
        PetInfo petInfo1 = new PetInfo();
        petInfo1.setWords("1,3,5"); // words 필드에 "1,3,5"를 포함하는 PetInfo

        PetInfo petInfo2 = new PetInfo();
        petInfo2.setWords("3,5,7"); // words 필드에 "3,5,7"을 포함하는 PetInfo

        PetInfo petInfo3 = new PetInfo();
        petInfo3.setWords("1,2,20"); // words 필드에 "1,2,20"을 포함하는 PetInfo

        // Mock PetInfo 리스트 생성
        List<PetInfo> mockPetInfos = Arrays.asList(petInfo1, petInfo2, petInfo3);

        // Mocking: userWordRepository.findWordIdsByUserId() 호출 시 mockWordIds 반환
        when(userWordRepository.findWordIdsByUserId(userId)).thenReturn(mockWordIds);

        // Mocking: petInfoRepository.findAll() 호출 시 mockPetInfos 반환
        when(petInfoRepository.findAll()).thenReturn(mockPetInfos);

        // 실제 테스트 수행: 서비스 메서드 호출
        List<PetInfo> top12PetInfos = wordMatchingService.getTop12PetInfosByUserWord(userId);

        // 검증: 반환된 리스트가 null이 아니고, 12개 이하의 PetInfo를 포함하는지 확인
        assertThat(top12PetInfos).isNotNull();
        assertThat(top12PetInfos.size()).isLessThanOrEqualTo(12);

        // 반환된 PetInfo의 words 필드가 예상한 매칭 단어 수와 일치하는지 확인
        // 매칭되는 단어의 수만 검증 (순서 상관 없음)
        int expectedMatchCount1 = 3; // "1,3,5"
        int expectedMatchCount2 = 2; // "3,5,7"
        int expectedMatchCount3 = 2; // "1,2,20"

        assertThat(countMatchingWords(petInfo1.getWords(), mockWordIds)).isEqualTo(expectedMatchCount1);
        assertThat(countMatchingWords(petInfo2.getWords(), mockWordIds)).isEqualTo(expectedMatchCount2);
        assertThat(countMatchingWords(petInfo3.getWords(), mockWordIds)).isEqualTo(expectedMatchCount3);
    }

    // countMatchingWords 메서드 (PetInfoService에서 동일한 로직을 사용)
    private int countMatchingWords(String words, List<Long> wordIds) {
        Set<Long> wordSet = Arrays.stream(words.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toSet());
        int matchCount = 0;
        for (Long wordId : wordIds) {
            if (wordSet.contains(wordId)) {
                matchCount++;
            }
        }
        return matchCount;
    }
}
