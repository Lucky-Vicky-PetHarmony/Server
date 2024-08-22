package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.board.Board;
import luckyvicky.petharmony.entity.board.BoardPin;
import luckyvicky.petharmony.entity.board.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardPinRepository extends JpaRepository<BoardPin, Long> {

    Optional<BoardPin> findByBoard_BoardIdAndUser_UserId(Long boardId, Long userId);

    Integer countByBoard_BoardId(Long boardId);
    // 사용자 ID에 해당하는 PIN 게시물들을 조회하는 메서드
    List<BoardPin> findByUser_UserId(Long userId);
}
