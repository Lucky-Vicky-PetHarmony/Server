package luckyvicky.petharmony.dto;

import luckyvicky.petharmony.entity.PetInfo;

public class WordClassificationDTO {
    private String desertionNo;
    private String specialMark;
    private String age;
    private String sexCd;
    private String wordId;

    // 기본 생성자
    public WordClassificationDTO() {}

    // 필드별로 값을 전달받는 생성자
    public WordClassificationDTO(String desertionNo, String specialMark, String age, String sexCd, String wordId) {
        this.desertionNo = desertionNo;
        this.specialMark = specialMark;
        this.age = age;
        this.sexCd = sexCd;
        this.wordId = wordId;
    }

    // Getter와 Setter
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

    // DTO에서 엔티티로 변환하는 메서드
    public PetInfo toEntity(PetInfo petInfo) {
        petInfo.setWordId(this.wordId); // 필요한 필드만 업데이트
        return petInfo;
    }
}
