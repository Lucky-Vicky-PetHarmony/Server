package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.PetLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetLikeRepository extends JpaRepository<PetLike, Long> {
    // 사용자 ID로 PetLike 조회
//    List<PetLike> findByUser_UserId(Long userId);
}
