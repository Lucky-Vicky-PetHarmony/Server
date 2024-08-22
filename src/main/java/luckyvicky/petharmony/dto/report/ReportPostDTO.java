package luckyvicky.petharmony.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportPostDTO {
    private Long reporterId; //신고자 id
    private Long reportedId; //피신고자 id
    private String reportType; //신고유형
    private String reportContent; //신고내용
    private String reportBoardOrComment; // 신고당한게 댓글인지 게시글인지
    private Long reportPostId; //신고당한 게시물이나 댓글의 id
}
