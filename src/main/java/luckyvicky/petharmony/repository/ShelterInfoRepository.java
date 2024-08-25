package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.ShelterInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShelterInfoRepository extends JpaRepository<ShelterInfo, Long> {

    Optional<ShelterInfo> findByCareNm(String careNm);

    // 모든 보호소 정보를 가져오는 메서드
    List<ShelterInfo> findAll();
}
