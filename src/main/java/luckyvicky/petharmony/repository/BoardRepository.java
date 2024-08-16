package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.board.Board;
import luckyvicky.petharmony.entity.board.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 댓글 수로 내림차순 정렬
    @Query("SELECT b FROM Board b LEFT JOIN Comment c ON c.board = b GROUP BY b ORDER BY COUNT(c) DESC")
    Page<Board> findAllOrderByCommentCountDesc(Pageable pageable);

    // 특정 카테고리에서 댓글 수로 내림차순 정렬
    @Query("SELECT b FROM Board b LEFT JOIN Comment c ON c.board = b WHERE b.category = :category GROUP BY b ORDER BY COUNT(c) DESC")
    Page<Board> findByCategoryOrderByCommentCountDesc(Category category, Pageable pageable);

    // 조회수로 내림차순 정렬
    @Query("SELECT b FROM Board b ORDER BY b.view DESC")
    Page<Board> findAllOrderByViewDesc(Pageable pageable);

    // 특정 카테고리에서 조회수로 내림차순 정렬
    @Query("SELECT b FROM Board b WHERE b.category = :category ORDER BY b.view DESC")
    Page<Board> findByCategoryOrderByViewDesc(Category category, Pageable pageable);

    // 업데이트 날짜로 내림차순 정렬
    @Query("SELECT b FROM Board b ORDER BY b.boardUpdate DESC")
    Page<Board> findAllOrderByBoardUpdateDesc(Pageable pageable);

    // 특정 카테고리에서 업데이트 날짜로 내림차순 정렬
    @Query("SELECT b FROM Board b WHERE b.category = :category ORDER BY b.boardUpdate DESC")
    Page<Board> findByCategoryOrderByBoardUpdateDesc(Category category, Pageable pageable);

}
