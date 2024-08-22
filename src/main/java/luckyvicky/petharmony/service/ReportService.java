package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.board.BoardListResponseDTO;
import luckyvicky.petharmony.dto.report.ReportDTO;
import luckyvicky.petharmony.dto.report.ReportListResponseDTO;
import luckyvicky.petharmony.entity.board.Report;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReportService {

    //신고
    String report(ReportDTO reportDTO);

    //신고리스트
    Page<ReportListResponseDTO> reportList(String selection, String sortBy, int page, int size);
}
