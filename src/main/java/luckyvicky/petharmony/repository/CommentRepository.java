package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.board.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoard_BoardId(Long boardId);

    void deleteByBoard_BoardId(Long boardId);

    Integer countByBoard_BoardId(Long boardId);

    // 사용자 ID에 해당하는 게시물들을 조회하는 메소드
    List<Comment> findByUser_UserId(Long userId);
}
