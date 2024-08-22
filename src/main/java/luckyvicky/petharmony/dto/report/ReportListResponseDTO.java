package luckyvicky.petharmony.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckyvicky.petharmony.entity.board.ReportProcess;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportListResponseDTO {
    private Long reportId; //신고 id
    private LocalDate reportDate; //신고 날짜
    private Long postId; //게시글(댓글)id
    private Long reporterId; //신고자 아이디
    private String reporterName; //신고자 이름
    private Long reportedId; //피신고자 아이디
    private String reportedName; //피신고자 이름
    private String reportContent; //신고내용
    private LocalDate processingDate; //처리날짜
    private ReportProcess reportProcess;//처리상태
    private String reportType;
}
