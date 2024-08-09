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
@ToString
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
    private LocalDateTime commCreate;   // 댓글 등록 날짜

    @UpdateTimestamp
    @Column(name = "comm_update", nullable = false)
    private LocalDateTime commUpdate;   // 댓글 수정 날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;                // 게시판 테이블

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;                  // 유저 테이블

}
