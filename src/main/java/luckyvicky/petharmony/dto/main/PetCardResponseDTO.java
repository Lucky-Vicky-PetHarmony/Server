package luckyvicky.petharmony.dto.main;

import lombok.*;
import luckyvicky.petharmony.entity.Word;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetCardResponseDTO {
    private String desertionNo;     // pet_info 테이블 id

    private String popFile;         // 이미지 경로

    private List<String> words;     // 단어

    private String kindCd;          // 종

    private String sexCd;           // 성별

    private String age;             // 나이

    private String weight;          // 몸무게

    private String orgNm;           // 보호 지역

    private String neuterYn;        // 중성화 여부
}
