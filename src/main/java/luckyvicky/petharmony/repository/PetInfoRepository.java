package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.dto.WordClassificationDTO;
import luckyvicky.petharmony.entity.PetInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PetInfoRepository extends JpaRepository<PetInfo, String> {

    // 페이징 메서드 추가
    Page<PetInfo> findAll(Pageable pageable);

    // WordClassificationDTO로 desertionNo와 specialMark만 반환하도록 JPQL 쿼리 수정
    @Query("SELECT new luckyvicky.petharmony.dto.WordClassificationDTO(p.desertionNo, p.specialMark) " +
            "FROM PetInfo p WHERE p.age = :age AND p.sexCd = :sexCd")
    Page<WordClassificationDTO> findWordClassificationsByCriteria(@Param("age") String age, @Param("sexCd") String sexCd, Pageable pageable);

    // words 필드를 LIKE 검색하여 wordId가 포함된 모든 PetInfo를 가져옴
    @Query("SELECT p FROM PetInfo p WHERE p.words LIKE CONCAT('%,', :wordId, ',%') OR p.words LIKE CONCAT(:wordId, ',%') OR p.words LIKE CONCAT('%,', :wordId) OR p.words = :wordId")
    List<PetInfo> findByWordId(@Param("wordId") String wordId);

    // 특정 careNm 목록에 해당하는 모든 PetInfo를 가져옴
    @Query("SELECT p FROM PetInfo p WHERE p.careNm IN :careNmList")
    List<PetInfo> findAllByCareNmIn(@Param("careNmList") List<String> careNmList);

    // notice_edt가 현재 날짜를 지난 PetInfo 조회
    List<PetInfo> findByNoticeEdtBefore(LocalDate currentDate);

    // notice_edt가 현재 날짜를 지나지 않은 PetInfo 조회
    List<PetInfo> findByNoticeEdtAfter(LocalDate currentDate);

    // desertionNo에 해당하는 PetInfo와 연결된 ShelterInfo를 조인하여 가져옴
    @Query("SELECT p FROM PetInfo p JOIN FETCH ShelterInfo s ON p.careNm = s.careNm WHERE p.desertionNo = :desertionNo")
    PetInfo findPetInfoWithShelterByDesertionNo(@Param("desertionNo") String desertionNo);

    // 유기동물 ID로 PetInfo 조회
    PetInfo findByDesertionNo(String desertionNo);

    // 카테고리별 입양동물 리스트
    Page<PetInfo> findByKindCdContaining(String kindCd, Pageable pageable);
}
