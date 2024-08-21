package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.report.ReportDTO;
import luckyvicky.petharmony.entity.board.Report;

public interface ReportService {
    String report(ReportDTO reportDTO);
}
