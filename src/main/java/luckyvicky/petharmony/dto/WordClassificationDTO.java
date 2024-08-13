package luckyvicky.petharmony.dto;

import luckyvicky.petharmony.entity.PetInfo;

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

    // Getter 및 Setter 메서드
    public String getDesertionNo() {
        return desertionNo;
    }

    public void setDesertionNo(String desertionNo) {
        this.desertionNo = desertionNo;
    }

    public String getSpecialMark() {
        return specialMark;
    }

    public void setSpecialMark(String specialMark) {
        this.specialMark = specialMark;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSexCd() {
        return sexCd;
    }

    public void setSexCd(String sexCd) {
        this.sexCd = sexCd;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    // 엔티티 업데이트 로직
    public void updateEntity(PetInfo petInfo) {
        if (this.words != null) {
            petInfo.updateWordId(this.words);
        }
    }
}
