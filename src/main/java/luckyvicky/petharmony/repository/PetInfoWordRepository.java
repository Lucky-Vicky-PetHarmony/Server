package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.PetInfoWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PetInfoWordRepository extends JpaRepository<PetInfoWord, Long> {

    // desertionNo가 있는지 확인하는 메서드 추가
    @Query("SELECT piw FROM PetInfoWord piw WHERE piw.petInfo.desertionNo = :desertionNo")
    List<PetInfoWord> findByDesertionNo(@Param("desertionNo") String desertionNo);
}
