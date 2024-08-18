package luckyvicky.petharmony.entity;

import jakarta.persistence.*;
import lombok.*;
import luckyvicky.petharmony.entity.board.Board;
import luckyvicky.petharmony.entity.board.Comment;
import luckyvicky.petharmony.security.Role;
import luckyvicky.petharmony.security.UserState;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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

    // 비밀번호 변경
    public void updatePassword(String password) {
        this.password = password;
    }
}
