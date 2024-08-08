package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.board.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
