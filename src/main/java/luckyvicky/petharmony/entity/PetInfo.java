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
    @Column(nullable = false, length = 15)
    private String desertion_no;

    @Column(length = 100)
    private String filename;

    @Temporal(TemporalType.DATE)
    private Date happen_dt;

    @Column(length = 100)
    private String happen_place;

    @Column(length = 50)
    private String kind_cd;

    @Column(length = 30)
    private String color_cd;

    @Column(length = 30)
    private String age;

    @Column(length = 30)
    private String weight;

    @Column(length = 30)
    private String notice_no;

    @Temporal(TemporalType.DATE)
    private Date notice_sdt;

    @Temporal(TemporalType.DATE)
    private Date notice_edt;

    @Column(length = 100)
    private String popfile;

    @Column(length = 10)
    private String process_state;

    @Column(length = 1)
    private char sex_cd;

    @Column(length = 1)
    private char neuter_yn;

    @Column(length = 200)
    private String special_mark;

    @Column(length = 50)
    private String care_nm;

    @Column(length = 20)
    private String charge_nm;

    @Column(length = 14)
    private String officetel;

    @Column(length = 200)
    private String notice_comment;
}
