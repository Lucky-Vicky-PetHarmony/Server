package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.ShelterInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShelterInfoRepository extends JpaRepository<ShelterInfo, Integer> {
    // careNm으로 ShelterInfo 엔티티 검색하는 메서드
    Optional<ShelterInfo> findByCareNm(String careNm);
}