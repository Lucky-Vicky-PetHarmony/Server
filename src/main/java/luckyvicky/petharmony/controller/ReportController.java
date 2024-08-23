package luckyvicky.petharmony.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.report.ReportDetailDTO;
import luckyvicky.petharmony.dto.report.ReportPostDTO;
import luckyvicky.petharmony.dto.report.ReportListResponseDTO;
import luckyvicky.petharmony.service.ReportService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/public/report")
public class ReportController {
    private final ReportService reportService;

    /**
     * 새로운 신고를 처리하는 메소드
     *
     * @param reportPostDTO 신고 정보가 담긴 DTO 객체.
     * @return 신고가 성공적으로 접수되었을 경우 성공 메시지를 반환하고,
     *         실패할 경우 에러 메시지를 반환
     */
    @PostMapping("/post")
    public ResponseEntity<String> report(@RequestBody ReportPostDTO reportPostDTO) {

        String reportMsg = reportService.report(reportPostDTO);

        if(Objects.equals(reportMsg, "report success")) {
            return ResponseEntity.ok("report success");
        }else {
            return ResponseEntity.badRequest().body("report failed");
        }
    }

    /**
     * 필터링 및 정렬 옵션을 통해 신고 목록을 페이징 처리하여 조회
     *
     * @param selectionString 필터 선택 기준을 콤마로 구분한 문자열.
     * @param sortBy 정렬 기준 (기본값은 날짜별 정렬).
     * @param page 조회할 페이지 번호 (0부터 시작).
     * @param size 페이지 당 레코드 수 (기본값은 8).
     * @return 조건에 맞는 신고 목록을 Page 객체로 반환
     */
    @GetMapping("/list")
    public Page<ReportListResponseDTO> reportList(@RequestParam(value = "selection", required = false) String selectionString,
                                                  @RequestParam(value = "sortBy", required = false, defaultValue = "date") String sortBy,
                                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "8") int size){

        return reportService.reportList(selectionString, sortBy, page, size);
    }

    /**
     * 특정 신고 ID에 해당하는 신고 상세 정보를 조회
     *
     * @param reportId 조회할 신고의 ID.
     * @return 신고 정보를 담고 있는 ReportDetailDTO 객체를 반환하며,
     *         신고가 존재하지 않을 경우 404 Not Found 응답을 반환
     */
    @GetMapping("/detail/{reportId}")
    public ResponseEntity<ReportDetailDTO> reportDetail(@PathVariable Long reportId){
        ReportDetailDTO reportDetailDTO = reportService.reportDetail(reportId);
        if(reportDetailDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reportDetailDTO);
    }

    /**
     * 신고 처리 상태를 업데이트
     *
     * @param reportId 처리할 신고의 ID.
     * @param processing 적용할 처리 상태 (예: DELETE_POST, THREE_DAY_SUSPENSION).
     * @return 처리 상태가 성공적으로 업데이트되면 성공 메시지를 반환하고,
     *         신고가 존재하지 않을 경우 404 Not Found 응답을 반환
     * @throws IOException 처리 중 오류가 발생할 경우 예외
     */
    @PutMapping("/processing/{reportId}")
    public ResponseEntity<String> reportPrecessing(@PathVariable Long reportId,
                                                   @RequestParam String processing) throws IOException {
        String processingResult = reportService.reportPrecessing(reportId, processing);
        if(processingResult == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(processingResult);
    }
}




