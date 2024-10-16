package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.UserWordDTO;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.entity.UserWord;
import luckyvicky.petharmony.entity.Word;
import luckyvicky.petharmony.repository.UserRepository;
import luckyvicky.petharmony.repository.UserWordRepository;
import luckyvicky.petharmony.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserWordService {

    private final UserRepository userRepository;
    private final WordRepository wordRepository;
    private final UserWordRepository userWordRepository;

    @Autowired
    public UserWordService(UserRepository userRepository, WordRepository wordRepository, UserWordRepository userWordRepository) {
        this.userRepository = userRepository;
        this.wordRepository = wordRepository;
        this.userWordRepository = userWordRepository;
    }

    /**
     * 사용자가 선택한 선호 단어들을 UserWord 테이블에 저장하는 메서드
     *
     * @param c 사용자가 선택한 단어 ID 목록과 사용자 ID를 포함하는 DTO
     */
    @Transactional
    public void saveUserWords(UserWordDTO userWordDTO) {
        User user = userRepository.findById(userWordDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        List<UserWord> userWord = userWordRepository.findByUser_UserId(userWordDTO.getUserId());
        if(!userWord.isEmpty()) {
            userWordRepository.deleteAll(userWord);
        }

        List<UserWord> userWords = userWordDTO.getWordId().stream().map(wordId -> {
            return UserWord.builder()
                    .user(user)
                    .word(wordRepository.findById(wordId)
                            .orElseThrow(() -> new RuntimeException("단어를 찾을 수 없습니다.")))
                    .build();
        }).collect(Collectors.toList());

        userWordRepository.saveAll(userWords);
    }

    /**
     * 특정 사용자가 선택한 모든 선호 단어 목록을 조회하는 메서드
     *
     * @param userId 사용자의 ID
     * @return 사용자가 선택한 UserWordDTO 목록
     */
    public UserWordDTO getUserWords(Long userId) {
        List<Long> wordIds = userWordRepository.findWordIdsByUserId(userId);

        return UserWordDTO.builder()
                .userId(userId)
                .wordId(wordIds)
                .build();
    }
}
