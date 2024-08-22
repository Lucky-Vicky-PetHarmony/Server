package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.board.Report;
import luckyvicky.petharmony.entity.board.ReportProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Page<Report> findByReportProcessingIn(List<ReportProcess> statuses, Pageable pageable);
}
