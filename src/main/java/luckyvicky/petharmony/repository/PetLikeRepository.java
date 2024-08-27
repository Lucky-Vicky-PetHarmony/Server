package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.PetLike;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

import java.util.List;

public interface PetLikeRepository extends JpaRepository<PetLike, Long> {
    // 사용자 ID로 PetLike 조회
//    List<PetLike> findByUser_UserId(Long userId);

    Optional<PetLike> findByUser_UserIdAndDesertionNo(Long user_userId, String desertionNo);

    // 사용자 ID와 반려동물의 desertionNo로 좋아요 기록을 조회하는 메서드
    Optional<PetLike> findByUserUserIdAndDesertionNo(Long userId, String desertionNo);

    // 특정 사용자가 좋아요한 반려동물의 목록을 조회하는 메서드
    List<PetLike> findByUserUserId(Long userId);
}
