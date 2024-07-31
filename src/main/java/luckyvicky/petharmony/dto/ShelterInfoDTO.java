package luckyvicky.petharmony.dto;

import java.util.Date;

public class ShelterInfoDTO {

    private int id;
    // 동물보호센터명
    private String careNm;
    // 관리기관명
    private String orgNm;
    // 동물보호센터유형
    private String divisionNm;
    // 구조대상동물
    private String saveTrgtAnimal;
    // 소재지번 도로명주소
    private String careAddr;
    // 소재지번 주소
    private String jibunAddr;
    // 위도
    private double lat;
    // 경도
    private double lng;
    // 동물보호센터 지정일자
    private Date dsignationDate;
    // 평일 운영 시작 시각
    private String weekOprStime;
    // 평일 운영 종료 시각
    private String weekOprEtime;
    // 평일 분양 시작 시각
    private String weekCellStime;
    // 평일 분양 종료 시각
    private String weekCellEtime;
    // 주말 운영 시작 시각
    private String weekendOprStime;
    // 주말 운영 종료 시각
    private String weekendOprEtime;
    // 주말 분양 시작 시각
    private String weekendCellStime;
    // 주말 분양 종료 시각
    private String weekendCellEtime;
    // 휴무일
    private String closeDay;
    // 수의사 인원수
    private int vetPersonCnt;
    // 사양관리사 인원수
    private int specsPersonCnt;
    // 진료실 수
    private int medicalCnt;
    // 사육실 수
    private int breedCnt;
    // 격리실 수
    private int quarabtineCnt;
    // 사료 보관실 수
    private int feedCnt;
    // 구조 운반용 차량 보유 수
    private int transCarCnt;
    // 전화번호
    private String careTel;
    // 데이터 기준일자
    private Date dataStdDt;

    // Getter 및 Setter 메소드
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCareNm() {
        return careNm;
    }

    public void setCareNm(String careNm) {
        this.careNm = careNm;
    }

    public String getOrgNm() {
        return orgNm;
    }

    public void setOrgNm(String orgNm) {
        this.orgNm = orgNm;
    }

    public String getDivisionNm() {
        return divisionNm;
    }

    public void setDivisionNm(String divisionNm) {
        this.divisionNm = divisionNm;
    }

    public String getSaveTrgtAnimal() {
        return saveTrgtAnimal;
    }

    public void setSaveTrgtAnimal(String saveTrgtAnimal) {
        this.saveTrgtAnimal = saveTrgtAnimal;
    }

    public String getCareAddr() {
        return careAddr;
    }

    public void setCareAddr(String careAddr) {
        this.careAddr = careAddr;
    }

    public String getJibunAddr() {
        return jibunAddr;
    }

    public void setJibunAddr(String jibunAddr) {
        this.jibunAddr = jibunAddr;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Date getDsignationDate() {
        return dsignationDate;
    }

    public void setDsignationDate(Date dsignationDate) {
        this.dsignationDate = dsignationDate;
    }

    public String getWeekOprStime() {
        return weekOprStime;
    }

    public void setWeekOprStime(String weekOprStime) {
        this.weekOprStime = weekOprStime;
    }

    public String getWeekOprEtime() {
        return weekOprEtime;
    }

    public void setWeekOprEtime(String weekOprEtime) {
        this.weekOprEtime = weekOprEtime;
    }

    public String getWeekCellStime() {
        return weekCellStime;
    }

    public void setWeekCellStime(String weekCellStime) {
        this.weekCellStime = weekCellStime;
    }

    public String getWeekCellEtime() {
        return weekCellEtime;
    }

    public void setWeekCellEtime(String weekCellEtime) {
        this.weekCellEtime = weekCellEtime;
    }

    public String getWeekendOprStime() {
        return weekendOprStime;
    }

    public void setWeekendOprStime(String weekendOprStime) {
        this.weekendOprStime = weekendOprStime;
    }

    public String getWeekendOprEtime() {
        return weekendOprEtime;
    }

    public void setWeekendOprEtime(String weekendOprEtime) {
        this.weekendOprEtime = weekendOprEtime;
    }

    public String getWeekendCellStime() {
        return weekendCellStime;
    }

    public void setWeekendCellStime(String weekendCellStime) {
        this.weekendCellStime = weekendCellStime;
    }

    public String getWeekendCellEtime() {
        return weekendCellEtime;
    }

    public void setWeekendCellEtime(String weekendCellEtime) {
        this.weekendCellEtime = weekendCellEtime;
    }

    public String getCloseDay() {
        return closeDay;
    }

    public void setCloseDay(String closeDay) {
        this.closeDay = closeDay;
    }

    public int getVetPersonCnt() {
        return vetPersonCnt;
    }

    public void setVetPersonCnt(int vetPersonCnt) {
        this.vetPersonCnt = vetPersonCnt;
    }

    public int getSpecsPersonCnt() {
        return specsPersonCnt;
    }

    public void setSpecsPersonCnt(int specsPersonCnt) {
        this.specsPersonCnt = specsPersonCnt;
    }

    public int getMedicalCnt() {
        return medicalCnt;
    }

    public void setMedicalCnt(int medicalCnt) {
        this.medicalCnt = medicalCnt;
    }

    public int getBreedCnt() {
        return breedCnt;
    }

    public void setBreedCnt(int breedCnt) {
        this.breedCnt = breedCnt;
    }

    public int getQuarabtineCnt() {
        return quarabtineCnt;
    }

    public void setQuarabtineCnt(int quarabtineCnt) {
        this.quarabtineCnt = quarabtineCnt;
    }

    public int getFeedCnt() {
        return feedCnt;
    }

    public void setFeedCnt(int feedCnt) {
        this.feedCnt = feedCnt;
    }

    public int getTransCarCnt() {
        return transCarCnt;
    }

    public void setTransCarCnt(int transCarCnt) {
        this.transCarCnt = transCarCnt;
    }

    public String getCareTel() {
        return careTel;
    }

    public void setCareTel(String careTel) {
        this.careTel = careTel;
    }

    public Date getDataStdDt() {
        return dataStdDt;
    }

    public void setDataStdDt(Date dataStdDt) {

    }
}
