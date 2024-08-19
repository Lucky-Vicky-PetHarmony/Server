package luckyvicky.petharmony.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckyvicky.petharmony.entity.board.Category;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardListResponseDTO {

    private Long boardId;

    private Long userId; //작성자 아이디

    private String boardTitle;

    private Category category;

    private Integer viewCount;

    private String boardCreate;

    private String boardUpdate;

    private Integer commentCount;

    private boolean image; //첨부파일 여부를 보여줌

    private Integer pinCount;
}
