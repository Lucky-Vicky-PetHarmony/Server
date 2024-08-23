package luckyvicky.petharmony.dto.mypage;

import lombok.*;
import luckyvicky.petharmony.entity.board.Category;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyCommentsDTO {
    private Long boardId;

    private Category category;

    private String boardTitle;

    private boolean image;

    private Integer viewCount;

    private Integer commentCount;

    private Integer pinCount;

    private String boardUpdate;

    private Long commId;

    private String content;
}
