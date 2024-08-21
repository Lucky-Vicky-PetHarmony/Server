package luckyvicky.petharmony.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.report.ReportDTO;
import luckyvicky.petharmony.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
