package luckyvicky.petharmony.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckyvicky.petharmony.entity.board.ReportProcess;
import luckyvicky.petharmony.entity.board.ReportType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDetailDTO {
    //기본정보
    private Long reportId;
    private LocalDateTime reportDate;
    private String reporterName;
    private ReportType reportType;
    private ReportProcess reportProcess;
    //신고당한 글정보
    private String postContent;
    private Long boardId;
    //신고내용
    private String reportContent;

    //해당 게시물의 신고목록
    private List<ReportDetailListDTO> reportDetailList = new ArrayList<>();


}
