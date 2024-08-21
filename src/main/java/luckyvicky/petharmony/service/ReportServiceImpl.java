package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.report.ReportDTO;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.entity.board.*;
import luckyvicky.petharmony.repository.BoardRepository;
import luckyvicky.petharmony.repository.CommentRepository;
import luckyvicky.petharmony.repository.ReportRepository;
import luckyvicky.petharmony.repository.UserRepository;
import org.springframework.stereotype.Service;

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
}
