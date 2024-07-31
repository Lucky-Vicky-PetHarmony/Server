package luckyvicky.petharmony.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "pet_info")
public class PetInfo {

    @Id
    @Column(name = "desertionNo", nullable = false, length = 15)
    private String desertionNo;

    @Column(name = "filename", length = 100)
    private String filename;

    @Column(name = "happenDt")
    @Temporal(TemporalType.DATE)
    private Date happenDt;

    @Column(name = "happenPlace", length = 100)
    private String happenPlace;

    @Column(name = "kindCd", length = 50)
    private String kindCd;

    @Column(name = "colorCd", length = 30)
    private String colorCd;

    @Column(name = "age", length = 30)
    private String age;

    @Column(name = "weight", length = 30)
    private String weight;

    @Column(name = "noticeNo", length = 30)
    private String noticeNo;

    @Column(name = "noticeSdt")
    @Temporal(TemporalType.DATE)
    private Date noticeSdt;

    @Column(name = "noticeEdt")
    @Temporal(TemporalType.DATE)
    private Date noticeEdt;

    @Column(name = "popfile", length = 100)
    private String popfile;

    @Column(name = "processState", length = 10)
    private String processState;

    @Column(name = "sexCd", length = 1)
    private char sexCd;

    @Column(name = "neuterYn", length = 1)
    private char neuterYn;

    @Column(name = "specialMark", length = 200)
    private String specialMark;

    @Column(name = "careNm", length = 50)
    private String careNm;

    @Column(name = "chargeNm", length = 20)
    private String chargeNm;

    @Column(name = "officetel", length = 14)
    private String officetel;

    @Column(name = "noticeComment", length = 200)
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
