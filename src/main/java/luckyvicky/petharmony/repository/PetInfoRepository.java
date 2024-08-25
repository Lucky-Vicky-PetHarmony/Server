package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.dto.WordClassificationDTO;
import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.entity.UserWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.util.List;

/**
 * PetInfoRepository는 PetInfo 엔티티와 데이터베이스 간의 상호작용을 처리하는 리포지토리 인터페이스
 * Spring Data JPA의 JpaRepository를 확장하여 기본적인 CRUD 기능을 제공
 */
public interface PetInfoRepository extends JpaRepository<PetInfo, String> {

    /**
     * 특정 연령(age)과 성별(sexCd)에 따라 필터링된 PetInfo 데이터를 조회하고,
     * 이를 WordClassificationDTO로 매핑하여 반환하는 커스텀 쿼리 메서드
     *
     * @param age  필터링할 반려동물의 연령
     * @param sexCd  필터링할 반려동물의 성별 코드
     * @param pageable  페이징 정보를 포함한 Pageable 객체
     * @return 필터링된 PetInfo 데이터를 기반으로 생성된 WordClassificationDTO의 페이지
     */
    @Query("SELECT new luckyvicky.petharmony.dto.WordClassificationDTO(p.desertionNo, p.specialMark, p.age, p.sexCd, '') " +
            "FROM PetInfo p WHERE p.age = :age AND p.sexCd = :sexCd")
    Page<WordClassificationDTO> findWordClassificationsByCriteria(@Param("age") String age, @Param("sexCd") String sexCd, Pageable pageable);

    // words 필드를 LIKE 검색하여 wordId가 포함된 모든 PetInfo를 가져옵니다.
    @Query("SELECT p FROM PetInfo p WHERE p.words LIKE CONCAT('%,', :wordId, ',%') OR p.words LIKE CONCAT(:wordId, ',%') OR p.words LIKE CONCAT('%,', :wordId) OR p.words = :wordId")
    List<PetInfo> findByWordId(@Param("wordId") String wordId);

    // shelter_info와 pet_info의 care_nm(위치 정보에 활용)
    @Query("SELECT p FROM PetInfo p WHERE p.careNm = :careNm")
    List<PetInfo> findAllByCareNm(@Param("careNm") String careNm);

    // notice_edt가 현재 날짜를 지난 PetInfo 조회
    List<PetInfo> findByNoticeEdtBefore(LocalDate currentDate);
}
