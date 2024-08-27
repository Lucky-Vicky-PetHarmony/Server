package luckyvicky.petharmony.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserWordDTO {
    private Long userId;     // 사용자 ID
    private List<Long> wordId;     // 단어 ID 리스트
}
