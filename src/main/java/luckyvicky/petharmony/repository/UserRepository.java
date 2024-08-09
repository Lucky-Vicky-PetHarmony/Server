package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Security에서 이메일을 사용해 인증 처리
    Optional<User> findByEmail(String email);

}
