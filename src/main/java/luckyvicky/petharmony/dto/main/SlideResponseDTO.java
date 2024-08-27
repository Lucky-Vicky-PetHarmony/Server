package luckyvicky.petharmony.dto.main;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlideResponseDTO {
    private String desertionNo;     // pet_info 테이블 id

    private String popFile;         // 이미지 경로

    private String orgNm;           // 보호 지역

    private String sexCd;           // 성별

    private String age;             // 나이
}
