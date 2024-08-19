package luckyvicky.petharmony.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardPinDTO {
    private Long userId;
    private Long boardId;
    private String pinAction;
}
