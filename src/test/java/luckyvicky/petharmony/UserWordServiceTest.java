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

    @BeforeEach
    public void setup() {
        userWordRepository.deleteAll();
        userRepository.deleteAll();
        wordRepository.deleteAll();

        user = User.builder()
                .userName("Test User")
                .email("test@example.com")
                .password("password")
                .phone("1234567890")
                .address("Test Address")
                .build();
        userRepository.save(user);

        word1 = Word.builder().wordSelect("단어1").build();
        word2 = Word.builder().wordSelect("단어2").build();
        wordRepository.save(word1);
        wordRepository.save(word2);
    }

    @Test
    public void testSaveUserWords() {
        List<UserWordDTO> userWordDTOs = Arrays.asList(
                new UserWordDTO(user.getUserId(), word1.getWordId()),
                new UserWordDTO(user.getUserId(), word2.getWordId())
        );

        userWordService.saveUserWords(userWordDTOs);

        List<UserWordDTO> savedUserWords = userWordService.getUserWords(user.getUserId());
        assertEquals(2, savedUserWords.size());
        assertTrue(savedUserWords.stream().anyMatch(dto -> dto.getWordId().equals(word1.getWordId())));
        assertTrue(savedUserWords.stream().anyMatch(dto -> dto.getWordId().equals(word2.getWordId())));
    }

    @Test
    public void testGetUserWords() {
        // Given
        List<UserWordDTO> userWordDTOs = Arrays.asList(
                new UserWordDTO(user.getUserId(), word1.getWordId()),
                new UserWordDTO(user.getUserId(), word2.getWordId())
        );

        userWordService.saveUserWords(userWordDTOs);

        // When
        List<UserWordDTO> savedUserWords = userWordService.getUserWords(user.getUserId());

        // Then
        assertEquals(2, savedUserWords.size());
        assertTrue(savedUserWords.stream().anyMatch(dto -> dto.getWordId().equals(word1.getWordId())));
        assertTrue(savedUserWords.stream().anyMatch(dto -> dto.getWordId().equals(word2.getWordId())));
    }
}
