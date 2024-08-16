package luckyvicky.petharmony.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentPostDTO {

    // 댓글 작성한 user id
    private Long userId;

    // 댓글 달린 게시물 boardId
    private Long boardId;

    // 댓글 내용
    private String commContent;
}
