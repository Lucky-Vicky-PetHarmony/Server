package luckyvicky.petharmony.controller;

import luckyvicky.petharmony.dto.UserWordDTO;
import luckyvicky.petharmony.service.UserWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserWordController {
    // UserWordService 주입
    @Autowired
    private UserWordService userWordService;

    /**
     * 사용자가 선택한 선호 단어들을 저장하는 API 엔드포인트
     *
     * @param userWordDTOs 사용자가 선택한 단어 ID 목록과 사용자 ID를 포함하는 DTO 목록
     */
    @PostMapping("/user/words")
    public void saveUserWords(@RequestBody List<UserWordDTO> userWordDTOs) {
        // UserWordService를 호출하여 사용자가 선택한 단어들을 저장
        userWordService.saveUserWords(userWordDTOs);
    }

    /**
     * 특정 사용자가 선택한 모든 선호 단어 목록을 조회하는 API 엔드포인트입니다.
     * @param userId 사용자의 ID입니다.
     * @return 사용자가 선택한 UserWordDTO 목록을 반환합니다.
     */
    @GetMapping("/user/{userId}/words")
    public List<UserWordDTO> getUserWords(@PathVariable Long userId) {
        // UserWordService를 호출하여 사용자가 선택한 단어 목록을 조회하고 반환합니다.
        return userWordService.getUserWords(userId);
    }
}
