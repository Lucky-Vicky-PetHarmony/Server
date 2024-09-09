package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.PetInfoWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetInfoWordRepository extends JpaRepository<PetInfoWord, Long> {
    Optional<List<PetInfoWord>> findByPetInfo(PetInfo petInfo);
}
