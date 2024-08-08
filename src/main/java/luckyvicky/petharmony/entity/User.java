package luckyvicky.petharmony.entity;

import jakarta.persistence.*;
import lombok.*;
import luckyvicky.petharmony.entity.board.Board;
import luckyvicky.petharmony.entity.board.Comment;
import luckyvicky.petharmony.security.Role;
import luckyvicky.petharmony.security.UserState;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;                           // 회원 ID

    @Column(name = "user_name", length = 100, nullable = false)
    private String userName;                       // 회원 이름

    @Column(length = 100, nullable = false, unique = true)
    private String email;                         // 회원 이메일

    @Column(length = 100, nullable = false)
    private String password;                      // 회원 비밀번호

    @Column(length = 100, nullable = false)
    private String phone;                         // 회원 전화번호

    @Column(length = 500)
    private String address;                       // 회원 주소

    @CreationTimestamp
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;             // 회원 가입일자

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;                            // (USER : 회원, ADMIN : 관리자)

    @Enumerated(EnumType.STRING)
    @Column(name = "user_state", nullable = false)
    private UserState userState;                  // 회원 상태 (ACTIVE, BANNED)

    // User엔티티에서 UserWord에 접근(특정 사용자가 선택한 모든 단어를 조회)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserWord> userWords;

    // User엔티티에서 PetLikes에 접근(사용자가 좋아요 누른 동물 조회)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetLike> petLikes;

    // User엔티티에서 Board에 접근(사용자가 작성한 글 조회)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards;

    // User엔티티에서 Comment에 접근(사용자가 작성한 댓글 조회)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}
