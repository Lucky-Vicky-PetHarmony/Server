package luckyvicky.petharmony.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "shelter_info")
public class ShelterInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "careNm", length = 255)
    private String careNm;

    @Column(name = "orgNm", length = 255)
    private String orgNm;

    @Column(name = "divisionNm", length = 255)
    private String divisionNm;

    @Column(name = "saveTrgtAnimal", length = 255)
    private String saveTrgtAnimal;

    @Column(name = "careAddr", length = 255)
    private String careAddr;

    @Column(name = "jibunAddr", length = 255)
    private String jibunAddr;

    @Column(name = "lat", precision = 10, scale = 7)
    private BigDecimal lat;

    @Column(name = "lng", precision = 10, scale = 7)
    private BigDecimal lng;

    @Column(name = "dsignationDate")
    @Temporal(TemporalType.DATE)
    private Date dsignationDate;

    @Column(name = "weekOprStime", length = 5)
    private String weekOprStime;

    @Column(name = "weekOprEtime", length = 5)
    private String weekOprEtime;

    @Column(name = "weekCellStime", length = 5)
    private String weekCellStime;

    @Column(name = "weekCellEtime", length = 5)
    private String weekCellEtime;

    @Column(name = "weekendOprStime", length = 5)
    private String weekendOprStime;

    @Column(name = "weekendOprEtime", length = 5)
    private String weekendOprEtime;

    @Column(name = "weekendCellStime", length = 5)
    private String weekendCellStime;

    @Column(name = "weekendCellEtime", length = 5)
    private String weekendCellEtime;

    @Column(name = "closeDay", length = 255)
    private String closeDay;

    @Column(name = "vetPersonCnt")
    private int vetPersonCnt;

    @Column(name = "specsPersonCnt")
    private int specsPersonCnt;

    @Column(name = "medicalCnt")
    private int medicalCnt;

    @Column(name = "breedCnt")
    private int breedCnt;

    @Column(name = "quarabtineCnt")
    private int quarabtineCnt;

    @Column(name = "feedCnt")
    private int feedCnt;

    @Column(name = "transCarCnt")
    private int transCarCnt;

    @Column(name = "careTel", length = 20)
    private String careTel;

    @Column(name = "dataStdDt")
    @Temporal(TemporalType.DATE)
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

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
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
        this.dataStdDt = dataStdDt;
    }
}
