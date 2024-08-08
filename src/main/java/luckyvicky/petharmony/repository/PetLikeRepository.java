package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.PetLike;
import luckyvicky.petharmony.entity.board.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetLikeRepository extends JpaRepository<PetLike, Long> {
}
