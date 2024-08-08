package luckyvicky.petharmony.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWordDTO {
    private Long userId; // 사용자 ID
    private Long wordId; // 단어 ID

    // 기본 생성자
    public UserWordDTO() {

    }

    // 모든 필드를 포함하는 생성자
    public UserWordDTO(Long userId, Long wordId) {
        this.userId = userId;
        this.wordId = wordId;
    }
}
