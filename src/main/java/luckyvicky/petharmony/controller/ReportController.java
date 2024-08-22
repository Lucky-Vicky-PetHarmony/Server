package luckyvicky.petharmony.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.board.BoardListResponseDTO;
import luckyvicky.petharmony.dto.report.ReportDTO;
import luckyvicky.petharmony.dto.report.ReportListResponseDTO;
import luckyvicky.petharmony.service.ReportService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/public/report")
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/post")
    public ResponseEntity<String> report(@RequestBody ReportDTO reportDTO) {

        String reportMsg = reportService.report(reportDTO);

        if(Objects.equals(reportMsg, "report success")) {
            return ResponseEntity.ok("report success");
        }else {
            return ResponseEntity.badRequest().body("report failed");
        }
    }

    @GetMapping("/list")
    public Page<ReportListResponseDTO> reportList(@RequestParam(value = "selection", required = false) String selectionString,
                                                  @RequestParam(value = "sortBy", required = false, defaultValue = "date") String sortBy,
                                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "8") int size){

        return reportService.reportList(selectionString, sortBy, page, size);
    }
}
