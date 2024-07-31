package luckyvicky.petharmony.dto;

import java.util.Date;

public class PetInfoDTO {

    // 유기번호
    private String desertionNo;

    // 유기 동물 사진
    private String filename;

    // 접수일
    private Date happenDt;

    // 발견 장소
    private String happenPlace;

    // 품종
    private String kindCd;

    // 색상
    private String colorCd;

    // 나이
    private String age;

    // 무게
    private String weight;

    // 공고 번호
    private String noticeNo;

    // 공고 시작일
    private Date noticeSdt;

    // 공고 종료일
    private Date noticeEdt;

    // 이미지 파일 경로
    private String popfile;

    // 처리 상태
    private String processState;

    // 성별 코드 (M: 수컷, F: 암컷, Q: 미상)
    private char sexCd;

    // 중성화 여부 (Y: 중성화됨, N: 중성화 안됨, U: 미상)
    private char neuterYn;

    // 특징
    private String specialMark;

    // 보호소 이름
    private String careNm;

    // 담당자 이름
    private String chargeNm;

    // 보호소 전화번호
    private String officetel;

    // 특이사항
    private String noticeComment;

    // Getter 및 Setter 메소드
    public String getDesertionNo() {
        return desertionNo;
    }

    public void setDesertionNo(String desertionNo) {
        this.desertionNo = desertionNo;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Date getHappenDt() {
        return happenDt;
    }

    public void setHappenDt(Date happenDt) {
        this.happenDt = happenDt;
    }

    public String getHappenPlace() {
        return happenPlace;
    }

    public void setHappenPlace(String happenPlace) {
        this.happenPlace = happenPlace;
    }

    public String getKindCd() {
        return kindCd;
    }

    public void setKindCd(String kindCd) {
        this.kindCd = kindCd;
    }

    public String getColorCd() {
        return colorCd;
    }

    public void setColorCd(String colorCd) {
        this.colorCd = colorCd;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getNoticeNo() {
        return noticeNo;
    }

    public void setNoticeNo(String noticeNo) {
        this.noticeNo = noticeNo;
    }

    public Date getNoticeSdt() {
        return noticeSdt;
    }

    public void setNoticeSdt(Date noticeSdt) {
        this.noticeSdt = noticeSdt;
    }

    public Date getNoticeEdt() {
        return noticeEdt;
    }

    public void setNoticeEdt(Date noticeEdt) {
        this.noticeEdt = noticeEdt;
    }

    public String getPopfile() {
        return popfile;
    }

    public void setPopfile(String popfile) {
        this.popfile = popfile;
    }

    public String getProcessState() {
        return processState;
    }

    public void setProcessState(String processState) {
        this.processState = processState;
    }

    public char getSexCd() {
        return sexCd;
    }

    public void setSexCd(char sexCd) {
        this.sexCd = sexCd;
    }

    public char getNeuterYn() {
        return neuterYn;
    }

    public void setNeuterYn(char neuterYn) {
        this.neuterYn = neuterYn;
    }

    public String getSpecialMark() {
        return specialMark;
    }

    public void setSpecialMark(String specialMark) {
        this.specialMark = specialMark;
    }

    public String getCareNm() {
        return careNm;
    }

    public void setCareNm(String careNm) {
        this.careNm = careNm;
    }

    public String getChargeNm() {
        return chargeNm;
    }

    public void setChargeNm(String chargeNm) {
        this.chargeNm = chargeNm;
    }

    public String getOfficetel() {
        return officetel;
    }

    public void setOfficetel(String officetel) {
        this.officetel = officetel;
    }

    public String getNoticeComment() {
        return noticeComment;
    }

    public void setNoticeComment(String noticeComment) {
        this.noticeComment = noticeComment;
    }
}
