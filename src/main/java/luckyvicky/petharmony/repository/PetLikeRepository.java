package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.PetLike;
import luckyvicky.petharmony.entity.board.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetLikeRepository extends JpaRepository<PetLike, Long> {
    Optional<PetLike> findByUser_UserIdAndDesertionNo(Long user_userId, String desertionNo);

}
