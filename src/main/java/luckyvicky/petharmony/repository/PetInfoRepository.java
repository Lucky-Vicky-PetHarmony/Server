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

    // 특정 careNm 목록에 해당하는 모든 PetInfo를 가져옴
    @Query("SELECT p FROM PetInfo p WHERE p.careNm IN :careNmList")
    List<PetInfo> findAllByCareNmIn(@Param("careNmList") List<String> careNmList);

    // notice_edt가 현재 날짜를 지난 PetInfo 조회
    List<PetInfo> findByNoticeEdtBefore(LocalDate currentDate);

    // notice_edt가 현재 날짜를 지나지 않은 PetInfo 조회
    List<PetInfo> findByNoticeEdtAfter(LocalDate currentDate);

    // 유기동물 ID로 PetInfo 조회
    PetInfo findByDesertionNo(String desertionNo);

    // 카테고리별 입양동물 리스트
    Page<PetInfo> findByKindCdContaining(String kindCd, Pageable pageable);

    // desertionNo와 specialMark만 페이징 처리하여 가져오는 쿼리
    @Query("SELECT new luckyvicky.petharmony.dto.WordClassificationDTO(p.desertionNo, p.specialMark) FROM PetInfo p")
    Page<WordClassificationDTO> findDesertionNoAndSpecialMarkPaged(Pageable pageable);
}
