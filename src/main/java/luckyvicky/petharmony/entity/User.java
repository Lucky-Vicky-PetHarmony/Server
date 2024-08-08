package luckyvicky.petharmony.entity;

import jakarta.persistence.*;
import lombok.*;
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

    // User엔티티에서 UserWord에 접근(특정 사용자가 선택한 모든 단어를 조회)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserWord> userWords; // 사용자가 선택한 단어 목록
}
