package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.board.Board;
import luckyvicky.petharmony.entity.board.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    //카테고리별 list
    Page<Board> findByCategory(Category category, Pageable pageable);


    // 검색기능(category == ALL)
    // 제목으로 검색
    Page<Board> findByBoardTitleContainingIgnoreCase(String title, Pageable pageable);

    // 내용으로 검색
    Page<Board> findByBoardContentContainingIgnoreCase(String content, Pageable pageable);

    // 제목 또는 내용으로 검색
    Page<Board> findByBoardTitleContainingIgnoreCaseOrBoardContentContainingIgnoreCase(
            String title, String content, Pageable pageable);

    // 검색기능(category != ALL)
    Page<Board> findByCategoryAndBoardTitleContainingIgnoreCase(Category category, String keyword, Pageable pageable);

    Page<Board> findByCategoryAndBoardContentContainingIgnoreCase(Category category, String keyword, Pageable pageable);

    Page<Board> findByCategoryAndBoardTitleContainingIgnoreCaseOrBoardContentContainingIgnoreCase(Category category, String titleKeyword, String contentKeyword, Pageable pageable);

    // 사용자 ID에 해당하는 게시물들을 조회하는 메소드
    List<Board> findByUser_UserId(Long userId);
}
