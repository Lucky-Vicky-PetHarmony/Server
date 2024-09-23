package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.board.BoardPin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardPinRepository extends JpaRepository<BoardPin, Long> {

    Optional<BoardPin> findByBoard_BoardIdAndUser_UserId(Long boardId, Long userId);

    Integer countByBoard_BoardIdAndUser_IsWithdrawalFalse(Long boardId);

    Page<BoardPin> findByUserUserId(Long userId, Pageable pageable);
}
