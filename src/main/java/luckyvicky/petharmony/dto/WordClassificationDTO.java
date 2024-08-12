package luckyvicky.petharmony.dto;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.Word;

public class WordClassificationDTO {
    private String desertionNo;
    private String specialMark;
    private String age;
    private String sexCd;
    private String wordId;

    public WordClassificationDTO() {}

    public WordClassificationDTO(String desertionNo, String specialMark, String age, String sexCd, String wordId) {
        this.desertionNo = desertionNo;
        this.specialMark = specialMark;
        this.age = age;
        this.sexCd = sexCd;
        this.wordId = wordId;
    }

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

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
    }

    public PetInfo toEntity(PetInfo petInfo, Word word) {
        petInfo.setWord(word);
        return petInfo;
    }
}
