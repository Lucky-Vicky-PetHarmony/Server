package luckyvicky.petharmony.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckyvicky.petharmony.entity.board.ReportType;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDetailListDTO {
    private Long reportId;
    private LocalDate reportDate;
    private String reporterName;
    private ReportType reportType;
}
