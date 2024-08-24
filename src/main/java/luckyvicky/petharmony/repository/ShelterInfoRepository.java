package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.ShelterInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShelterInfoRepository extends JpaRepository<ShelterInfo, Long> {

    /**
     * 주어진 careNm에 해당하는 ShelterInfo를 검색합니다.
     *
     * @param careNm 검색할 careNm
     * @return 주어진 careNm에 해당하는 ShelterInfo의 Optional 객체
     */
    Optional<ShelterInfo> findByCareNm(String careNm);

    // 위도, 경도를 기준으로 거리 계산하여 30km 이내의 보호소를 검색하는 네이티브 쿼리
    @Query(value = "SELECT * FROM shelter_info s WHERE ST_Distance_Sphere(POINT(s.lat, s.lng), POINT(:lat, :lon)) <= :distance * 1000", nativeQuery = true)
    List<ShelterInfo> findWithinDistance(@Param("lat") double lat, @Param("lon") double lon, @Param("distance") double distance);

    // 위도, 경도를 기준으로 가까운 순서대로 보호소를 검색하는 네이티브 쿼리
    @Query(value = "SELECT * FROM shelter_info s ORDER BY ST_Distance_Sphere(POINT(s.lat, s.lng), POINT(:lat, :lon)) ASC", nativeQuery = true)
    List<ShelterInfo> findNearestShelters(@Param("lat") double lat, @Param("lon") double lon, Pageable pageable);
}
