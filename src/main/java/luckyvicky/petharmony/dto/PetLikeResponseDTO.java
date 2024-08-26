package luckyvicky.petharmony.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetLikeResponseDTO {
    private Long likeId;
    private String desertionNo;
    private Long userId; // User 정보를 포함
}
