package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Security에서 이메일을 사용해 인증 처리
    Optional<User> findByEmail(String email);
    // 전화번호로 아이디(이메일) 찾기
    Optional<User> findByPhone(String phone);
    // 카카오 회원 ID로 회원가입 || 로그인
    Optional<User> findByKakaoId(Long kakaoId);
}
