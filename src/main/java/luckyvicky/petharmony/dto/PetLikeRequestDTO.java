package luckyvicky.petharmony.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetLikeRequestDTO {
    private Long userId;           // 사용자의 ID
    private String desertionNo;    // 반려동물의 유기 번호 (String으로 수정)
    private boolean isLiked;       // 좋아요 여부 (true: 좋아요, false: 좋아요 취소)
}
