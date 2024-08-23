package luckyvicky.petharmony.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWordDTO {
    private Long userWordId; // UserWord 테이블의 ID
    private Long userId;     // 사용자 ID
    private Long wordId;     // 단어 ID

    // 기본 생성자
    public UserWordDTO() {
    }

    // 모든 필드를 포함하는 생성자
    public UserWordDTO(Long userWordId, Long userId, Long wordId) {
        this.userWordId = userWordId;
        this.userId = userId;
        this.wordId = wordId;
    }
}
