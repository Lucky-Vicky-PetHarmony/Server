package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.board.BoardListResponseDTO;
import luckyvicky.petharmony.dto.report.ReportDTO;
import luckyvicky.petharmony.dto.report.ReportListResponseDTO;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.entity.board.*;
import luckyvicky.petharmony.repository.BoardRepository;
import luckyvicky.petharmony.repository.CommentRepository;
import luckyvicky.petharmony.repository.ReportRepository;
import luckyvicky.petharmony.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    /**
     * @param reportDTO 신고내용에 관한 정보들이 담겨있는 DTO
     * @return
     */
    @Override
    public String report(ReportDTO reportDTO) {

        //신고자와 피신고자
        User reporter = userRepository.findById(reportDTO.getReporterId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지않은 신고자 ID: "+reportDTO.getReporterId()));
        User reported = userRepository.findById(reportDTO.getReportedId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지않은 피신고자 ID: "+reportDTO.getReportedId()));
        Board board = null;
        Comment comment = null;

        // 신고당한게 댓글인지 게시물인지
        if(reportDTO.getReportBoardOrComment().equals("board")){
            board = boardRepository.findById(reportDTO.getReportPostId())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지않은 board ID: "+reportDTO.getReportPostId()));
        }else {
            comment = commentRepository.findById(reportDTO.getReportPostId())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지않은 comment ID: "+reportDTO.getReportPostId()));
        }

        Report report = Report.builder()
                .reporter(reporter)
                .reported(reported)
                .reportType(ReportType.valueOf(reportDTO.getReportType()))
                .reportContent(reportDTO.getReportContent())
                .board(board)
                .comment(comment)
                .reportProcessing(ReportProcess.UNPROCESSED)
                .build();
        reportRepository.save(report);
        return "report success";
    }

    /**
     * @param selectionString 선택된 값 리스트(미처리, 보류, 처리완료)
     * @param sortBy 정렬
     * @param page 요청 페이지
     * @param size 페이지 사이즈
     */
    @Override
    public Page<ReportListResponseDTO> reportList(String selectionString, String sortBy, int page, int size) {

        //클라이언트에서 문자열로 받은 처리필터를 리스트로 변환
        String[] selection = selectionString.split(",");

        Sort sort;
        if ("dateReverse".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Direction.ASC, "reportDate"); //오래된순
        } else {
            sort = Sort.by(Sort.Direction.DESC, "reportDate"); // 기본 정렬: 최신순
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Report> reportPage;

        // select 에서 선택한 값만 보이게
        if (selectionString.isEmpty()) {
            reportPage = reportRepository.findAll(pageable);
        } else {
            List<ReportProcess> statuses = new ArrayList<>();
            boolean includeCompleted = false;

            for (String status : selection) {
                if ("UNPROCESSED".equals(status)) {
                    statuses.add(ReportProcess.UNPROCESSED);
                } else if ("PENDING".equals(status)) {
                    statuses.add(ReportProcess.PENDING);
                } else if ("COMPLETED".equals(status)) {
                    // 처리완료된 상태들
                    statuses.add(ReportProcess.THREE_DAY_SUSPENSION);
                    statuses.add(ReportProcess.DELETE_POST);
                    statuses.add(ReportProcess.ACCOUNT_TERMINATION);
                    statuses.add(ReportProcess.IGNORE_REPORT);
                }
            }
            reportPage = reportRepository.findByReportProcessingIn(statuses, pageable);
        }

        return reportPage.map(this::buildReportListResponseDTO);
    }

    private ReportListResponseDTO buildReportListResponseDTO(Report report) {

        return ReportListResponseDTO.builder()
                .reportId(report.getReportId())
                .reportDate(report.getReportDate())
                .postId(report.getBoard()!=null?
                        report.getBoard().getBoardId():
                        report.getComment().getCommId())
                .reporterId(report.getReporter().getUserId())
                .reporterName(report.getReporter().getUserName())
                .reportedId(report.getReported().getUserId())
                .reportedName(report.getReported().getUserName())
                .reportContent(report.getReportContent())
                .processingDate(report.getProcessingDate())
                .reportProcess(report.getReportProcessing())
                .reportType(String.valueOf(report.getReportType()))
                .build();
    }

}
