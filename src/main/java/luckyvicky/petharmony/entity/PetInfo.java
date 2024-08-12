package luckyvicky.petharmony.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "pet_info")
public class PetInfo {

    @Id
    @Column(name = "desertion_no", nullable = false, length = 15)
    private String desertionNo;

    @Temporal(TemporalType.DATE)
    @Column(name = "happen_dt")
    private Date happenDt;

    @Column(name = "happen_place", length = 100)
    private String happenPlace;

    @Column(name = "kind_cd", length = 50)
    private String kindCd;

    @Column(name = "color_cd", length = 30)
    private String colorCd;

    @Column(name = "age", length = 30)
    private String age;

    @Column(name = "weight", length = 30)
    private String weight;

    @Column(name = "notice_no", length = 30)
    private String noticeNo;

    @Temporal(TemporalType.DATE)
    @Column(name = "notice_sdt")
    private Date noticeSdt;

    @Temporal(TemporalType.DATE)
    @Column(name = "notice_edt")
    private Date noticeEdt;

    @Column(name = "popfile", length = 100)
    private String popfile;

    @Column(name = "process_state", length = 10)
    private String processState;

    @Column(name = "sex_cd", length = 1)
    private char sexCd;

    @Column(name = "neuter_yn", length = 1)
    private char neuterYn;

    @Column(name = "special_mark", length = 200)
    private String specialMark;

    @Column(name = "care_nm", length = 50)
    private String careNm;

    @Column(name = "charge_nm", length = 20)
    private String chargeNm;

    @Column(name = "officetel", length = 14)
    private String officetel;

    @Column(name = "notice_comment", length = 200)
    private String noticeComment;

    @ManyToOne
    @JoinColumn(name = "word_id") // 외래 키로 매핑
    private Word word;
}
