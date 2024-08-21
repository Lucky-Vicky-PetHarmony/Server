package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 사용자 조회
    Optional<User> findByEmail(String email);
    // 아이디 찾기 : 카카오 회원 여부와 전화번호로 사용자 조회
    Optional<User> findByPhoneAndKakaoIdIsNull(String phone);
    // 카카오 ID로 사용자 조회
    Optional<User> findByKakaoId(String kakaoId);
}
