package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.UserWordDTO;
import luckyvicky.petharmony.entity.UserWord;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private UserWordRepository userWordRepository;

    /**
     * 사용자가 선택한 선호 단어들을 UserWord 테이블에 저장하는 메서드
     *
     * @param userWordDTOs 사용자가 선택한 단어 ID 목록과 사용자 ID를 포함하는 DTO 목록
     */
    @Transactional
    public void saveUserWords(List<UserWordDTO> userWordDTOs) {
        List<UserWord> userWords = userWordDTOs.stream().map(dto -> {
            return new UserWord(
                    null,
                    userRepository.findById(dto.getUserId())
                            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다.")),
                    wordRepository.findById(dto.getWordId())
                            .orElseThrow(() -> new RuntimeException("단어를 찾을 수 없습니다."))
            );
        }).collect(Collectors.toList());

        userWordRepository.saveAll(userWords);
    }

    /**
     * 특정 사용자가 선택한 모든 선호 단어 목록을 조회하는 메서드
     *
     * @param userId 사용자의 ID
     * @return 사용자가 선택한 UserWordDTO 목록
     */
    public List<UserWordDTO> getUserWords(Long userId) {
        return userWordRepository.findByUserUserId(userId).stream()
                .map(userWord -> new UserWordDTO(userWord.getUser().getUserId(), userWord.getWord().getWordId()))
                .collect(Collectors.toList());
    }
}
