package luckyvicky.petharmony.entity.board;

import jakarta.persistence.*;
import lombok.*;
import luckyvicky.petharmony.entity.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;                // id

    @CreationTimestamp
    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;         // 신고날짜

    @Column(name = "report_type", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    private ReportType reportType;   // 신고항목

    @Column(name = "report_content", nullable = false)
    private String reportContent;   // 신고내용

    @Column(name = "report_processing", nullable = false)
    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    private ReportProcess reportProcessing;   // 신고처리

    @Column(name = "processing_date")
    private LocalDate processingDate;         // 처리날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;                // 게시판 테이블

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comm_id")
    private Comment comment;                // 댓글 테이블

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;                  // 유저 테이블 : 신고자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_id")
    private User reported;                  // 유저 테이블 : 피신고자

}
