package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.dto.WordClassificationDTO;
import luckyvicky.petharmony.entity.PetInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * PetInfoRepository는 PetInfo 엔티티와 데이터베이스 간의 상호작용을 처리하는 리포지토리 인터페이스
 * Spring Data JPA의 JpaRepository를 확장하여 기본적인 CRUD 기능을 제공
 */
public interface PetInfoRepository extends JpaRepository<PetInfo, String> {

    /**
     * 특정 연령(age)과 성별(sexCd)에 따라 필터링된 PetInfo 데이터를 조회하고,
     * 이를 WordClassificationDTO로 매핑하여 반환하는 커스텀 쿼리 메서드
     *
     * 주요 동작:
     * 1. 이 메서드는 JPQL(Java Persistence Query Language)을 사용하여 쿼리를 수행
     * 2. PetInfo 엔티티에서 age와 sexCd 조건에 맞는 레코드를 필터링
     * 3. 필터링된 레코드들을 WordClassificationDTO 객체로 매핑
     *    이때, WordClassificationDTO의 wordId 필드는 빈 문자열('')로 설정
     *    (필요에 따라 이 부분은 이후 로직에서 채워질 수 있음.)
     * 4. 페이징 처리된 결과를 Page<WordClassificationDTO> 형태로 반환
     *    Page 객체는 데이터 리스트와 함께 페이지 정보(전체 페이지 수, 현재 페이지, 총 아이템 수 등)를 포함
     */
    @Query("SELECT new luckyvicky.petharmony.dto.WordClassificationDTO(p.desertionNo, p.specialMark, p.age, p.sexCd, '') " +
            "FROM PetInfo p WHERE p.age = :age AND p.sexCd = :sexCd")
    Page<WordClassificationDTO> findWordClassificationsByCriteria(String age, String sexCd, Pageable pageable);
}
