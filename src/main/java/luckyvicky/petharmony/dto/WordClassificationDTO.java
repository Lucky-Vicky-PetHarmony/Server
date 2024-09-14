package luckyvicky.petharmony.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckyvicky.petharmony.entity.PetInfo;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordClassificationDTO {

    private String desertionNo;
    private String specialMark;

}
