package luckyvicky.petharmony.dto;

import luckyvicky.petharmony.entity.PetInfo;

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

    // Getter 및 Setter 메서드 (필요한 경우에만 사용)
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

    // 엔티티 업데이트 로직
    public void updateEntity(PetInfo petInfo) {
        petInfo.updateWordId(Long.valueOf(this.wordId));
    }

    // 추가적으로 필요한 DTO 내부의 데이터 처리 로직을 여기에 추가
}
