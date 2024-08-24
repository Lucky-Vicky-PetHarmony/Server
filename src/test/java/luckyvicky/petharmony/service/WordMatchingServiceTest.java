package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.repository.PetInfoRepository;
import luckyvicky.petharmony.repository.UserWordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WordMatchingServiceTest {

    // PetInfoRepository와 UserWordRepository를 Mock으로 설정
    @Mock
    private PetInfoRepository petInfoRepository;

    @Mock
    private UserWordRepository userWordRepository;

    // WordMatchingService의 인스턴스를 생성하고 Mock을 주입
    @InjectMocks
    private WordMatchingService wordMatchingService;

    // 각 테스트 실행 전에 Mock 객체를 초기화
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMatchingPetInfosByUserWord_UserHasNoWords() {
        // Given: 특정 사용자(userId)가 선택한 단어가 없는 경우를 설정
        Long userId = 1L;
        when(userWordRepository.findWordIdsByUserId(userId)).thenReturn(List.of()); // 빈 리스트 반환 설정

        // When: WordMatchingService의 getMatchingPetInfosByUserWord 메서드를 호출
        List<PetInfo> result = wordMatchingService.getMatchingPetInfosByUserWord(userId);

        // Then: 결과가 빈 리스트인지 확인하고, petInfoRepository의 findByWordId 메서드가 호출되지 않았는지 검증
        assertThat(result).isEmpty();
        verify(petInfoRepository, never()).findByWordId(anyString());
    }

    @Test
    void testGetMatchingPetInfosByUserWord_UserHasWords() {
        // Given: 특정 사용자(userId)가 선택한 단어가 있는 경우를 설정
        Long userId = 1L;
        List<Long> wordIds = Arrays.asList(101L, 102L); // 사용자가 선택한 단어 ID 목록
        PetInfo pet1 = new PetInfo();
        pet1.setDesertionNo("1");
        pet1.setWords("101,103");

        PetInfo pet2 = new PetInfo();
        pet2.setDesertionNo("2");
        pet2.setWords("102,104");

        // Mock 설정: UserWordRepository와 PetInfoRepository의 반환값 설정
        when(userWordRepository.findWordIdsByUserId(userId)).thenReturn(wordIds);
        when(petInfoRepository.findByWordId("101")).thenReturn(List.of(pet1));
        when(petInfoRepository.findByWordId("102")).thenReturn(List.of(pet2));

        // When: WordMatchingService의 getMatchingPetInfosByUserWord 메서드를 호출
        List<PetInfo> result = wordMatchingService.getMatchingPetInfosByUserWord(userId);

        // Then: 반환된 결과가 예상한 PetInfo 객체들을 포함하는지 확인
        assertThat(result).hasSize(2);
        assertThat(result).extracting("desertionNo").containsExactlyInAnyOrder("1", "2");
    }

    @Test
    void testGetWordIdListAsString_UserHasNoWords() {
        // Given: 특정 사용자(userId)가 선택한 단어가 없는 경우를 설정
        Long userId = 1L;
        when(userWordRepository.findWordIdsByUserId(userId)).thenReturn(List.of()); // 빈 리스트 반환 설정

        // When: WordMatchingService의 getWordIdListAsString 메서드를 호출
        String result = wordMatchingService.getWordIdListAsString(userId);

        // Then: 반환된 결과가 빈 문자열인지 확인
        assertThat(result).isEmpty();
    }

    @Test
    void testGetWordIdListAsString_UserHasWords() {
        // Given: 특정 사용자(userId)가 선택한 단어가 있는 경우를 설정
        Long userId = 1L;
        when(userWordRepository.findWordIdsByUserId(userId)).thenReturn(List.of(101L, 102L)); // 단어 ID 리스트 반환 설정

        // When: WordMatchingService의 getWordIdListAsString 메서드를 호출
        String result = wordMatchingService.getWordIdListAsString(userId);

        // Then: 반환된 결과가 예상한 문자열 "101,102"와 일치하는지 확인
        assertThat(result).isEqualTo("101,102");
    }

    @Test
    void testHasMatchingWords_MatchFound() {
        // Given: words와 wordIdListAsString이 일부 일치하는 경우를 설정
        String words = "101,102,103";
        String wordIdListAsString = "102,104";

        // When: WordMatchingService의 hasMatchingWords 메서드를 호출
        boolean result = wordMatchingService.hasMatchingWords(words, wordIdListAsString);

        // Then: 반환된 결과가 true인지 확인 (일치하는 단어 존재)
        assertThat(result).isTrue();
    }

    @Test
    void testHasMatchingWords_NoMatchFound() {
        // Given: words와 wordIdListAsString이 일치하지 않는 경우를 설정
        String words = "101,102,103";
        String wordIdListAsString = "104,105";

        // When: WordMatchingService의 hasMatchingWords 메서드를 호출
        boolean result = wordMatchingService.hasMatchingWords(words, wordIdListAsString);

        // Then: 반환된 결과가 false인지 확인 (일치하는 단어 없음)
        assertThat(result).isFalse();
    }

    @Test
    void testCountMatchingWords_MatchFound() {
        // Given: words와 wordIdListAsString이 일부 일치하는 경우를 설정
        String words = "101,102,103";
        String wordIdListAsString = "102,103,104";

        // When: WordMatchingService의 countMatchingWords 메서드를 호출
        int result = wordMatchingService.countMatchingWords(words, wordIdListAsString);

        // Then: 반환된 결과가 2인지 확인 (일치하는 단어 2개)
        assertThat(result).isEqualTo(2);
    }

    @Test
    void testCountMatchingWords_NoMatchFound() {
        // Given: words와 wordIdListAsString이 일치하지 않는 경우를 설정
        String words = "101,102,103";
        String wordIdListAsString = "104,105";

        // When: WordMatchingService의 countMatchingWords 메서드를 호출
        int result = wordMatchingService.countMatchingWords(words, wordIdListAsString);

        // Then: 반환된 결과가 0인지 확인 (일치하는 단어 없음)
        assertThat(result).isEqualTo(0);
    }
}
