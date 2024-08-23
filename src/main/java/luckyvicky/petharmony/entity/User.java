package luckyvicky.petharmony.entity;

import jakarta.persistence.*;
import lombok.*;
import luckyvicky.petharmony.dto.mypage.MyProfileRequestDTO;
import luckyvicky.petharmony.security.Role;
import luckyvicky.petharmony.security.UserState;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
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

    @Column(name = "kakao_id")
    private String kakaoId;                       // 카카오 회원 ID

    @Column(name = "is_withdrawal")
    private Boolean isWithdrawal;                 // 탈퇴 여부

    @Column(name = "suspension_until")
    private LocalDate suspensionUntil;                 // 정지 여부(정지 마지막 날짜)

    // 비밀번호 변경
    public void updatePassword(String password) {
        this.password = password;
    }
  
    // 탈퇴처리
    public void activeIsWithdrawal() { this.isWithdrawal = true; }

    // 정지처리(정지시킬 기간을 받아서 처리), 정지와 동시에 BANNED
    public void updateSuspensionUntil(int days) {
        this.suspensionUntil = this.suspensionUntil!=null?
                this.suspensionUntil.plusDays(days):
                LocalDate.now().plusDays(days);
        this.userState = UserState.BANNED;
    }

    // 정지처리해제
    public void releaseBans() {
        this.userState = UserState.ACTIVE;
    }
  
    // [마이페이지] - 내 정보 수정(이름, 이메일, 전화번호)
    public void updateUserInfo(MyProfileRequestDTO myProfileRequestDTO) {
        this.userName = myProfileRequestDTO.getUserName();
        this.email = myProfileRequestDTO.getEmail();
        this.phone = myProfileRequestDTO.getPhone();
    }
}
