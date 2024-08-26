package luckyvicky.petharmony.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetLikeRequestDTO {
    private Long userId;
    private String desertionNo;
}
