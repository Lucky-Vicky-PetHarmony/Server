package luckyvicky.petharmony.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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

    @Column(name = "save_trgt_animal", length = 255)
    private String saveTrgtAnimal;

    @Column(name = "care_addr", length = 255)
    private String careAddr;

    @Column(name = "lat", precision = 10, scale = 7)
    private BigDecimal lat;

    @Column(name = "lng", precision = 10, scale = 7)
    private BigDecimal lng;

    @Column(name = "week_opr_stime", length = 5)
    private String weekOprStime;

    @Column(name = "week_opr_etime", length = 5)
    private String weekOprEtime;

    @Column(name = "weekend_opr_stime", length = 5)
    private String weekendOprStime;

    @Column(name = "weekend_opr_etime", length = 5)
    private String weekendOprEtime;

    @Column(name = "close_day", length = 255)
    private String closeDay;

    @Column(name = "care_tel", length = 20)
    private String careTel;
}
