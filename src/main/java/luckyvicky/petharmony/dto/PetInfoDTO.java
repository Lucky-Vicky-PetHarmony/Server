package luckyvicky.petharmony.dto;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetInfoDTO {

    // 유기번호
    private String desertionNo;

    // 접수일
    private Date happenDt;

    // 발견 장소
    private String happenPlace;

    // 품종
    private String kindCd;

    // 색상
    private String colorCd;

    // 나이
    private String age;

    // 무게
    private String weight;

    // 공고 번호
    private String noticeNo;

    // 공고 시작일
    private Date noticeSdt;

    // 공고 종료일
    private Date noticeEdt;

    // 이미지 파일 경로
    private String popfile;

    // 처리 상태
    private String processState;

    // 성별 코드 (M: 수컷, F: 암컷, Q: 미상)
    private char sexCd;

    // 중성화 여부 (Y: 중성화됨, N: 중성화 안됨, U: 미상)
    private char neuterYn;

    // 특징
    private String specialMark;

}
