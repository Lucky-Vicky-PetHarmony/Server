package luckyvicky.petharmony.entity.board;

import jakarta.persistence.*;
import lombok.*;
import luckyvicky.petharmony.entity.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;            // id

    @Column(name = "board_title", length = 100, nullable = false)
    private String boardTitle;       // 게시물 제목

    @Column(name = "board_content", length = 500, nullable = false)
    private String boardContent;     // 게시물 내용

    @Builder.Default
    @Column(nullable = false)
    private Integer view = 0;   // 게시물 조회수

    @CreationTimestamp
    @Column(name = "board_create", nullable = false, updatable = false)
    private LocalDateTime boardCreate;   // 게시물 생성 날짜

    @CreationTimestamp
    @Column(name = "board_update", nullable = false)
    private LocalDateTime boardUpdate;   // 게시물 업데이트 날짜

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) //카테고리 이름을 그대로 저장
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;                  // 유저 테이블

    @Formula("(select count(*) from comment c where c.board_id = board_id)")
    private int commentCount;

    @Formula("(select count(*) " +
            "from board_pin bp join user u on bp.user_id = u.user_id " +
            "where bp.board_id = board_id and u.is_withdrawal = false)")
    private int pinCount;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    public void isDeleteActive() {
        this.isDeleted = true;
    }

    public void viewCount() {
        this.view++;
    }

    public void updateBoard(String newContent, String newTitle, Category category) {
        this.boardContent = newContent;
        this.boardTitle = newTitle;
        this.category = category;
        this.boardUpdate = LocalDateTime.now();
    }
}
