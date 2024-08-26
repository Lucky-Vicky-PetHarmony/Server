package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.PetLike;
import luckyvicky.petharmony.entity.board.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetLikeRepository extends JpaRepository<PetLike, Long> {
    // 주어진 사용자 ID로 PetLike 목록을 조회하는 메서드
    List<PetLike> findByUser_UserId(Long userId);
}
