package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.board.Board;
import luckyvicky.petharmony.entity.board.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByIsDeletedFalse(Pageable pageable);

    //카테고리별 list
    Page<Board> findByCategoryAndIsDeletedFalse(Category category, Pageable pageable);


    // 검색기능(category == ALL)
    // 제목으로 검색
    Page<Board> findByBoardTitleContainingIgnoreCaseAndIsDeletedFalse(String title, Pageable pageable);

    // 내용으로 검색
    Page<Board> findByBoardContentContainingIgnoreCaseAndIsDeletedFalse(String content, Pageable pageable);

    // 제목 또는 내용으로 검색
    Page<Board> findByBoardTitleContainingIgnoreCaseOrBoardContentContainingIgnoreCaseAndIsDeletedFalse(
            String title, String content, Pageable pageable);

    // 검색기능(category != ALL)
    Page<Board> findByCategoryAndBoardTitleContainingIgnoreCaseAndIsDeletedFalse(Category category, String keyword, Pageable pageable);

    Page<Board> findByCategoryAndBoardContentContainingIgnoreCaseAndIsDeletedFalse(Category category, String keyword, Pageable pageable);

    Page<Board> findByCategoryAndBoardTitleContainingIgnoreCaseOrBoardContentContainingIgnoreCaseAndIsDeletedFalse(Category category, String titleKeyword, String contentKeyword, Pageable pageable);

    // 페이지네이션 적용
    Page<Board> findByUserUserId(Long userId, Pageable pageable);
}
