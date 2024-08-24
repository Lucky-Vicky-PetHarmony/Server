package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.UserWordDTO;
import luckyvicky.petharmony.service.UserWordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserWordServiceTest {

    @Autowired
    private UserWordService userWordService;

    @Test
    public void testGetUserWords() {
        Long userId = 27L;
        List<UserWordDTO> userWordList = userWordService.getUserWords(userId);

        // UserWordDTO 리스트 출력
        userWordList.forEach(userWord -> {
            System.out.println("User ID: " + userWord.getUserId() + ", Word ID: " + userWord.getWordId());
        });
    }

}
