package luckyvicky.petharmony.dto.main;

import lombok.*;
import luckyvicky.petharmony.entity.Word;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetCardResponseDTO {
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
