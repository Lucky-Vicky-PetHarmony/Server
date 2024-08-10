package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.board.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoard_BoardId(Long boardId);

    void deleteByBoard_BoardId(Long boardId);
}
