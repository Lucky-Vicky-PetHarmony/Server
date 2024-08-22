package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.report.ReportDetailDTO;
import luckyvicky.petharmony.dto.report.ReportPostDTO;
import luckyvicky.petharmony.dto.report.ReportListResponseDTO;
import org.springframework.data.domain.Page;

public interface ReportService {

    //신고
    String report(ReportPostDTO reportPostDTO);

    //신고리스트
    Page<ReportListResponseDTO> reportList(String selection, String sortBy, int page, int size);

    //신고 상세
    ReportDetailDTO reportDetail(Long reportId);
}
