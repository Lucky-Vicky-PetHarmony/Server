package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.ShelterInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShelterInfoRepository extends JpaRepository<ShelterInfo, Integer> {
    // 커스텀 쿼리 메소드는 여기에 정의
}