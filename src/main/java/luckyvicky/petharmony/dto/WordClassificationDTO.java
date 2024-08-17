package luckyvicky.petharmony.dto;

import lombok.Data;
import luckyvicky.petharmony.entity.PetInfo;

@Data
public class WordClassificationDTO {
    private String desertionNo;
    private String specialMark;
    private String age;
    private String sexCd;
    private String words;

    public WordClassificationDTO() {}

    public WordClassificationDTO(String desertionNo, String specialMark, String age, String sexCd, String words) {
        this.desertionNo = desertionNo;
        this.specialMark = specialMark;
        this.age = age;
        this.sexCd = sexCd;
        this.words = words;
    }

    // 엔티티 업데이트 로직
    public void updateEntity(PetInfo petInfo) {
        if (this.words != null && !this.words.isEmpty()) {
            petInfo.updateWordId(this.words);
        }
    }
}
