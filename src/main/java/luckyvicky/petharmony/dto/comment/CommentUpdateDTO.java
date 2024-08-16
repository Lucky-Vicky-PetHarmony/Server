package luckyvicky.petharmony.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateDTO {
    private Long userId;
    private Long commId;
    private String commContent;
}
