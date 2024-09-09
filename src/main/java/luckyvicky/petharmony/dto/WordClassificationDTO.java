package luckyvicky.petharmony.dto;

import lombok.Data;
import luckyvicky.petharmony.entity.PetInfo;

@Data
public class WordClassificationDTO {
    private String desertionNo;
    private String specialMark;

    public WordClassificationDTO() {}

    public WordClassificationDTO(String desertionNo, String specialMark) {
        this.desertionNo = desertionNo;
        this.specialMark = specialMark;
    }

}
