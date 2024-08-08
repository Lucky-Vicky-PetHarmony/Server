package luckyvicky.petharmony;

import luckyvicky.petharmony.dto.UserWordDTO;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.entity.Word;
import luckyvicky.petharmony.repository.UserRepository;
import luckyvicky.petharmony.repository.UserWordRepository;
import luckyvicky.petharmony.repository.WordRepository;
import luckyvicky.petharmony.service.UserWordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserWordServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private UserWordRepository userWordRepository;

    @Autowired
    private UserWordService userWordService;

    private User user;
    private Word word1;
    private Word word2;

    /**
     * 각 테스트 전에 실행되는 메서드로, 테스트 데이터를 초기화
     * 여기서는 모든 테이블을 비우고, 테스트에 필요한 기본 데이터를 삽입
     */
    @BeforeEach
    public void setup() {
        // 모든 UserWord 엔티티 삭제
        userWordRepository.deleteAll();
        // 모든 User 엔티티 삭제
        userRepository.deleteAll();
        // 모든 Word 엔티티 삭제
        wordRepository.deleteAll();

        // 새로운 User 엔티티 생성 및 저장
        user = User.builder()
                .userName("Test User")
                .email("test@example.com")
                .password("password")
                .phone("1234567890")
                .address("Test Address")
                .build();
        userRepository.save(user);

        // 새로운 Word 엔티티 생성 및 저장
        word1 = Word.builder().wordSelect("단어1").build();
        word2 = Word.builder().wordSelect("단어2").build();
        wordRepository.save(word1);
        wordRepository.save(word2);
    }

    /**
     * 사용자가 선택한 단어들을 UserWord 테이블에 저장하고, 올바르게 저장되었는지 검증하는 테스트
     */
    @Test
    public void testSaveUserWords() {
        // 사용자가 선택한 단어 ID 목록 생성
        List<UserWordDTO> userWordDTOs = Arrays.asList(
                new UserWordDTO(user.getUserId(), word1.getWordId()),
                new UserWordDTO(user.getUserId(), word2.getWordId())
        );

        // UserWordService를 통해 단어들을 저장
        userWordService.saveUserWords(userWordDTOs);

        // 저장된 단어 목록을 조회
        List<UserWordDTO> savedUserWords = userWordService.getUserWords(user.getUserId());

        // 저장된 단어 목록의 크기가 2인지 검증
        assertEquals(2, savedUserWords.size());

        // 저장된 단어 목록에 word1과 word2가 각각 포함되어 있는지 검증
        assertTrue(savedUserWords.stream().anyMatch(dto -> dto.getWordId().equals(word1.getWordId())));
        assertTrue(savedUserWords.stream().anyMatch(dto -> dto.getWordId().equals(word2.getWordId())));
    }

    /**
     * 특정 사용자가 선택한 모든 선호 단어 목록을 조회하고, 올바르게 조회되는지 검증하는 테스트
     */
    @Test
    public void testGetUserWords() {
        // 사용자가 선택한 단어 ID 목록 생성
        List<UserWordDTO> userWordDTOs = Arrays.asList(
                new UserWordDTO(user.getUserId(), word1.getWordId()),
                new UserWordDTO(user.getUserId(), word2.getWordId())
        );

        // UserWordService를 통해 단어들을 저장
        userWordService.saveUserWords(userWordDTOs);

        // 저장된 단어 목록을 조회
        List<UserWordDTO> savedUserWords = userWordService.getUserWords(user.getUserId());

        // 저장된 단어 목록의 크기가 2인지 검증
        assertEquals(2, savedUserWords.size());

        // 저장된 단어 목록에 word1과 word2가 각각 포함되어 있는지 검증
        assertTrue(savedUserWords.stream().anyMatch(dto -> dto.getWordId().equals(word1.getWordId())));
        assertTrue(savedUserWords.stream().anyMatch(dto -> dto.getWordId().equals(word2.getWordId())));
    }
}
