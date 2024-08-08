package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 세션 범위를 벗어난 엔팉티 지연 로딩된 컬렉션 접근 방지 (필요한 관계 즉시 로딩)
    @EntityGraph(attributePaths = {"userWords"})
    Optional<User> findById(Long id);
}
