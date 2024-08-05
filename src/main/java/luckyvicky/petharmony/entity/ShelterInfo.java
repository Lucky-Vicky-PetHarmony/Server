package luckyvicky.petharmony.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "shelter_info")
public class ShelterInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 255)
    private String care_nm;

    @Column(length = 255)
    private String org_nm;

    @Column(length = 255)
    private String division_nm;

    @Column(length = 255)
    private String save_trgt_animal;

    @Column(length = 255)
    private String care_addr;

    @Column(length = 255)
    private String jibun_addr;

    @Column(precision = 10, scale = 7)
    private BigDecimal lat;

    @Column(precision = 10, scale = 7)
    private BigDecimal lng;

    @Temporal(TemporalType.DATE)
    private Date dsignation_date;

    @Column(length = 5)
    private String week_opr_stime;

    @Column(length = 5)
    private String week_opr_etime;

    @Column(length = 5)
    private String week_cell_stime;

    @Column(length = 5)
    private String week_cell_etime;

    @Column(length = 5)
    private String weekend_opr_stime;

    @Column(length = 5)
    private String weekend_opr_etime;

    @Column(length = 5)
    private String weekend_cell_stime;

    @Column(length = 5)
    private String weekend_cell_etime;

    @Column(length = 255)
    private String close_day;

    private int vet_person_cnt;

    private int specs_person_cnt;

    private int medical_cnt;

    private int breed_cnt;

    private int quarantine_cnt;

    private int feed_cnt;

    private int trans_car_cnt;

    @Column(length = 20)
    private String care_tel;

    @Temporal(TemporalType.DATE)
    private Date data_std_dt;

}
