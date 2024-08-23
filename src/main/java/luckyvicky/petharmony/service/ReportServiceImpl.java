package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.report.ReportDetailDTO;
import luckyvicky.petharmony.dto.report.ReportDetailListDTO;
import luckyvicky.petharmony.dto.report.ReportPostDTO;
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

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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
     * 신고하기
     *
     * @param reportPostDTO 신고내용에 관한 정보들이 담겨있는 DTO
     * @return 신고 성공 여부를 반환하는 문자열 ("report success" 또는 "report failed")
     */
    @Override
    public String report(ReportPostDTO reportPostDTO) {

        //신고자와 피신고자
        User reporter = userRepository.findById(reportPostDTO.getReporterId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지않은 신고자 ID: "+ reportPostDTO.getReporterId()));
        User reported = userRepository.findById(reportPostDTO.getReportedId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지않은 피신고자 ID: "+ reportPostDTO.getReportedId()));
        Board board = null;
        Comment comment = null;

        // 신고당한게 댓글인지 게시물인지
        if(reportPostDTO.getReportBoardOrComment().equals("board")){
            board = boardRepository.findById(reportPostDTO.getReportPostId())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지않은 board ID: "+ reportPostDTO.getReportPostId()));
        }else {
            comment = commentRepository.findById(reportPostDTO.getReportPostId())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지않은 comment ID: "+ reportPostDTO.getReportPostId()));
        }

        Report report = Report.builder()
                .reporter(reporter)
                .reported(reported)
                .reportType(ReportType.valueOf(reportPostDTO.getReportType()))
                .reportContent(reportPostDTO.getReportContent())
                .board(board)
                .comment(comment)
                .reportProcessing(ReportProcess.UNPROCESSED)
                .build();
        reportRepository.save(report);
        return "report success";
    }

    /**
     * 신고목록
     *
     * @param selectionString 선택된 값 리스트(미처리, 보류, 처리완료)
     * @param sortBy 정렬
     * @param page 요청 페이지
     * @param size 페이지 사이즈
     * @return 선택된 필터와 정렬 기준에 따라 페이징된 신고 목록을 반환
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

    /**
     * 신고상세
     *
     * @param reportId reportId 조회할 신고 ID
     * @return 신고 상세 정보 DTO
     */
    @Override
    public ReportDetailDTO reportDetail(Long reportId) {

        // 요청받은 reportId에 해당하는 Report객체
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지않은 reportID"+reportId));

        //해당 신고를 제외한 신고목록
        List<Report> otherReports = new ArrayList<>();
        if(report.getBoard()!=null){
            otherReports = reportRepository.findByBoard_BoardIdAndReportIdNot(report.getBoard().getBoardId(), reportId);
        }else {
            otherReports = reportRepository.findByComment_CommIdAndReportIdNot(report.getComment().getCommId(), reportId);
        }

        // 신고목록을 DTO 객체 리스트로 변환
        List<ReportDetailListDTO> reportDetailListDTOS =
                otherReports.stream().map(this::buildReportDetailListDTO).toList();

        // ReportDetailDTO 변환해서 리턴
        return ReportDetailDTO.builder()
                .reportId(reportId)
                .reportDate(report.getReportDate())
                .reporterName(report.getReporter().getUserName())
                .reportType(report.getReportType())
                .reportProcess(report.getReportProcessing())
                .postContent(report.getBoard()!=null?
                        report.getBoard().getBoardContent():
                        report.getComment().getCommContent())
                .boardId(report.getBoard()!=null?
                        report.getBoard().getBoardId():
                        report.getComment().getBoard().getBoardId())
                .reportContent(report.getReportContent())
                .reportDetailList(reportDetailListDTOS)
                .build();
    }

    /**
     * 신고처리
     *
     * @param reportId 처리할 reportId
     * @param processing 처리할 상태 (예: 삭제, 3일 정지, 회원 탈퇴 등)
     * @return 처리 결과 메시지 (처리 상태)
     */
    @Override
    public String reportPrecessing(Long reportId, String processing) throws IOException {

        // 요청받은 reportId에 해당하는 Report객체
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지않은 reportID"+reportId));

        if(Objects.equals(processing, "DELETE_POST")){

            // 실제 데이터에서 지우는 것이 아닌 논리적 삭제
            if(report.getBoard()!=null){
                Board board = report.getBoard();
                board.isDeleteActive();
            }else {
                Comment comment = report.getComment();
                comment.isDeleteActive();
            }
        }else if(Objects.equals(processing, "THREE_DAY_SUSPENSION")){
            //TODO: 3일정지
        }else if(Objects.equals(processing, "ACCOUNT_TERMINATION")){
            //TODO: 회원탈퇴
        }else if(Objects.equals(processing, "UNPROCESSED")){
            return processing;
        }

        //해당 신고를 제외한 신고목록
        List<Report> otherReports = new ArrayList<>();
        if(report.getBoard()!=null){
            otherReports = reportRepository.findByBoard_BoardId(report.getBoard().getBoardId());
        }else {
            otherReports = reportRepository.findByComment_CommId(report.getComment().getCommId());
        }
        //신고목록들 처리상태 업데이트
        List<Report> updatedReports = otherReports.stream()
                .map(otherReport -> otherReport.toBuilder()
                        .reportProcessing(ReportProcess.valueOf(processing))  // 새로운 처리상태로 변경
                        .processingDate(LocalDate.now())
                        .build())
                .collect(Collectors.toList());

        // 변경된 객체들을 저장
        reportRepository.saveAll(updatedReports);
        return processing;
    }

    //report -> ReportDetailListDTO
    private ReportDetailListDTO buildReportDetailListDTO(Report report) {
        return ReportDetailListDTO.builder()
                .reportId(report.getReportId())
                .reportDate(report.getReportDate())
                .reporterName(report.getReporter().getUserName())
                .reportType(report.getReportType())
                .build();
    }

    //report -> ReportListResponseDTO
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
