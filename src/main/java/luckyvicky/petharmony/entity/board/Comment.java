package luckyvicky.petharmony.entity.board;

import jakarta.persistence.*;
import lombok.*;
import luckyvicky.petharmony.entity.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comm_id")
    private Long commId;                // id

    @Column(name = "comm_content", nullable = false)
    private String commContent;         // 댓글 내용

    @CreationTimestamp
    @Column(name = "comm_create", nullable = false, updatable = false)
    private LocalDateTime commCreate;   // 댓글 등록 시간

    @UpdateTimestamp
    @Column(name = "comm_update", nullable = false)
    private LocalDateTime commUpdate;   // 댓글 수정 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;                // 게시판 테이블

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;                  // 유저 테이블

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    public void isDeleteActive() {
        this.isDeleted = true;
    }

    public void updateContent(String newContent) {
        this.commContent = newContent;
        // updateTime은 @UpdateTimestamp에 의해 자동으로 갱신
    }

}
