package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.board.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i.imageId FROM Image i WHERE i.board.boardId = :boardId")
    List<Long> findImageIdsByBoardId(Long boardId);

//    @Query("FROM Image i WHERE i.board.boardId = :boardId")
    List<Image> findByBoard_BoardId(Long boardId);
}
