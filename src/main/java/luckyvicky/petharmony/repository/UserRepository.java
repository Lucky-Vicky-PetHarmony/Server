package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 사용자 조회
    Optional<User> findByEmail(String email);
    // 아이디 찾기 : 카카오 회원 여부와 전화번호로 사용자 조회
    Optional<User> findByPhoneAndKakaoIdIsNull(String phone);
    // 카카오 ID로 사용자 조회
    Optional<User> findByKakaoId(String kakaoId);
    // 탈퇴한 사용자 중 이메일로 사용자 조회
    Optional<User> findByIsWithdrawalTrueAndEmail(String email);
    //오늘 날짜랑 suspensionUntil이랑 같은 유저 조회
    List<User> findBySuspensionUntil(LocalDate today);
}
