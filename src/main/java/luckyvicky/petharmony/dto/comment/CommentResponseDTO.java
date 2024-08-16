package luckyvicky.petharmony.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {
    private Long commId;
    private Long userId; //댓글작성자인지 확인하기 위한 userId
    private String userName; //화면에 보여질 이름
    private String content;
    private String commCreate;
    private String commUpdate;
}
