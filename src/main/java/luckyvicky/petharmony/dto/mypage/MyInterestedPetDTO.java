package luckyvicky.petharmony.dto.mypage;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyInterestedPetDTO {
    private String desertionNo;

    private String popFile;

    private List<String> words;

    private String kindCd;

    private String sexCd;

    private String age;

    private String weight;

    private String orgNm;

    private String neuterYn;
}