package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    // 인증번호 확인을 위한 전화번호 검색 기준 중 가장 최근 항목
    Optional<Certification> findTopByPhoneOrderByCreateDateDesc(String phone);
}
