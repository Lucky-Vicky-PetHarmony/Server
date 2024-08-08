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

    // UserRepository 주입
    @Autowired
    private UserRepository userRepository;

    // WordRepository 주입
    @Autowired
    private WordRepository wordRepository;

    // UserWordRepository 주입
    @Autowired
    private UserWordRepository userWordRepository;

    /**
     * 사용자가 선택한 선호 단어들을 UserWord 테이블에 저장하는 메서드
     *
     * @param userWordDTOs 사용자가 선택한 단어 ID 목록과 사용자 ID를 포함하는 DTO 목록
     */
    @Transactional
    public void saveUserWords(List<UserWordDTO> userWordDTOs) {
        // 각 UserWordDTO를 순회하며 처리
        for (UserWordDTO userWordDTO : userWordDTOs) {
            // 사용자 ID를 사용하여 User 엔티티를 조회
            User user = userRepository.findById(userWordDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            // 단어 ID를 사용하여 Word 엔티티를 조회
            Word word = wordRepository.findById(userWordDTO.getWordId())
                    .orElseThrow(() -> new RuntimeException("단어를 찾을 수 없습니다."));

            // 새로운 UserWord 엔티티를 생성
            UserWord userword = new UserWord();
            // UserWord 엔티티에 User 엔티티를 설정
            userword.assignUser(user);
            // UserWord 엔티티를 데이터베이스에 저장
            userword.assignWord(word);

            // UserWord 엔티티를 데이터베이스에 저장
            userWordRepository.save(userword);
        }
    }

    /**
     * 특정 사용자가 선택한 모든 선호 단어 목록을 조회하는 메서드
     * @param userId 사용자의 ID
     * @return 사용자가 선택한 UserWordDTO 목록
     */
    public List<UserWordDTO> getUserWords(Long userId) {
        // 사용자 ID를 사용하여 User 엔티티를 조회합니다.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // User 엔티티의 userWords 리스트를 UserWordDTO 리스트로 변환하여 반환합니다.
        return user.getUserWords().stream()
                .map(userWord -> new UserWordDTO(
                        userWord.getUser().getUserId(),
                        userWord.getWord().getWordId()
                ))
                .collect(Collectors.toList());
    }
}
