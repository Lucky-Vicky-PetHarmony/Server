package luckyvicky.petharmony.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pet_info")
public class PetInfo {

    @Id
    @Column(name = "desertion_no", length = 15)
    private String desertionNo;

    @Column(name = "happen_dt")
    @Temporal(TemporalType.DATE)
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

    @Column(name = "notice_sdt")
    @Temporal(TemporalType.DATE)
    private Date noticeSdt;

    @Column(name = "notice_edt")
    @Temporal(TemporalType.DATE)
    private Date noticeEdt;

    @Column(name = "popfile", length = 100)
    private String popfile;

    @Column(name = "process_state", length = 10)
    private String processState;

    @Column(name = "sex_cd", length = 1)
    private String sexCd;

    @Column(name = "neuter_yn", length = 1)
    private String neuterYn;

    @Column(name = "special_mark", length = 200)
    private String specialMark;

    @Column(name = "care_nm", length = 50)
    private String careNm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_nm", referencedColumnName = "care_nm", insertable = false, updatable = false)
    private ShelterInfo shelterInfo;

    @OneToMany(mappedBy = "petInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PetInfoWord> petInfoWords;
}
