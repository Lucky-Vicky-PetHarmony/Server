package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.board.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c " +
            "LEFT JOIN FETCH c.user " +
            "LEFT JOIN FETCH c.board " +
            "WHERE c.board.boardId = :boardId")
    List<Comment> findCommentsWithUserAndBoard(@Param("boardId") Long boardId);

    List<Comment> findByBoard_BoardId(Long boardId);

    void deleteByBoard_BoardId(Long boardId);

    // 사용자 ID에 해당하는 게시물들을 조회하는 메소드
    List<Comment> findByUser_UserId(Long userId);
}
