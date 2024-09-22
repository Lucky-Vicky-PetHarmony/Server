package luckyvicky.petharmony.dto.mypage;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyInterestedPetDTO {
    private String desertion_no;

    private String popfile;

    private List<String> words;

    private String kind_cd;

    private String sex_cd;

    private String age;

    private String weight;

    private String org_nm;

    private String neuter_yn;

    private Boolean pet_like;
}
