package luckyvicky.petharmony.dto;

import lombok.Data;

@Data
public class PetInfoWordDTO {

    private String desertionNo;
    private Long wordId;

    public PetInfoWordDTO(String desertionNo, Long wordId) {
        this.desertionNo = desertionNo;
        this.wordId = wordId;
    }
}
