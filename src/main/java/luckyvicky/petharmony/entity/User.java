package luckyvicky.petharmony.entity;

import jakarta.persistence.*;
import lombok.*;
import luckyvicky.petharmony.entity.board.Board;
import luckyvicky.petharmony.entity.board.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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

    @Column(length = 100, nullable = false)
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


}
