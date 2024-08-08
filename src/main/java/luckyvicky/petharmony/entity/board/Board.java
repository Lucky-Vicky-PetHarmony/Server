package luckyvicky.petharmony.entity.board;

import jakarta.persistence.*;
import lombok.*;
import luckyvicky.petharmony.entity.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;            // id

    @Column(length = 100, nullable = false)
    private String boardTitle;       // 게시물 제목

    @Column(length = 500, nullable = false)
    private String boardContent;     // 게시물 내용

    @Builder.Default
    @Column(nullable = false)
    private Integer view = 0;   // 게시물 조회수

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime boardCreateDate;   // 게시물 생성 날짜

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime boardModifyDate;   // 게시물 업데이트 날짜

    @Column(nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;                  // 유저 테이블

    // Board엔티티에서 Comment에 접근(게시물에 작성된 댓글 조회)
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    // Board엔티티에서 Image에 접근(게시물에 작성된 첨부파일 조회)
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;
}
