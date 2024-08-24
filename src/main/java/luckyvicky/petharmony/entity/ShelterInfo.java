package luckyvicky.petharmony.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "shelter_info")
public class ShelterInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "care_nm", length = 255)
    private String careNm;

    @Column(name = "org_nm", length = 255)
    private String orgNm;

    @Column(name = "division_nm", length = 255)
    private String divisionNm;

    @Column(name = "save_trgt_animal", length = 255)
    private String saveTrgtAnimal;

    @Column(name = "care_addr", length = 255)
    private String careAddr;

    @Column(name = "jibun_addr", length = 255)
    private String jibunAddr;

    @Column(name = "lat", precision = 10, scale = 7)
    private BigDecimal lat;

    @Column(name = "lng", precision = 10, scale = 7)
    private BigDecimal lng;

    @Column(name = "dsignation_date")
    @Temporal(TemporalType.DATE)
    private Date dsignationDate;

    @Column(name = "week_opr_stime", length = 5)
    private String weekOprStime;

    @Column(name = "week_opr_etime", length = 5)
    private String weekOprEtime;

    @Column(name = "week_cell_stime", length = 5)
    private String weekCellStime;

    @Column(name = "week_cell_etime", length = 5)
    private String weekCellEtime;

    @Column(name = "weekend_opr_stime", length = 5)
    private String weekendOprStime;

    @Column(name = "weekend_opr_etime", length = 5)
    private String weekendOprEtime;

    @Column(name = "weekend_cell_stime", length = 5)
    private String weekendCellStime;

    @Column(name = "weekend_cell_etime", length = 5)
    private String weekendCellEtime;

    @Column(name = "close_day", length = 255)
    private String closeDay;

    @Column(name = "vet_person_cnt")
    private int vetPersonCnt;

    @Column(name = "specs_person_cnt")
    private int specsPersonCnt;

    @Column(name = "medical_cnt")
    private int medicalCnt;

    @Column(name = "breed_cnt")
    private int breedCnt;

    @Column(name = "quarantine_cnt")
    private int quarantineCnt;

    @Column(name = "feed_cnt")
    private int feedCnt;

    @Column(name = "trans_car_cnt")
    private int transCarCnt;

    @Column(name = "care_tel", length = 20)
    private String careTel;

    @Column(name = "data_std_dt")
    @Temporal(TemporalType.DATE)
    private Date dataStdDt;

}
