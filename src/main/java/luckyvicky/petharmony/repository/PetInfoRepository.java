package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.dto.WordClassificationDTO;
import luckyvicky.petharmony.entity.PetInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PetInfoRepository extends JpaRepository<PetInfo, String> {

    @Query("SELECT new luckyvicky.petharmony.dto.WordClassificationDTO(p.desertionNo, p.specialMark, p.age, p.sexCd, '') " +
            "FROM PetInfo p WHERE p.age = :age AND p.sexCd = :sexCd")
    Page<WordClassificationDTO> findWordClassificationsByCriteria(String age, String sexCd, Pageable pageable);
}
