package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.UserWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWordRepository extends JpaRepository<UserWord, Long> {

    /**
     * 특정 사용자가 선택한 모든 word_id 리스트를 반환하는 메서드
     *
     * @param userId 사용자의 ID
     * @return 해당 사용자가 선택한 word_id 리스트
     */
    @Query("SELECT uw.word.wordId FROM UserWord uw WHERE uw.user.userId = :userId")
    List<Long> findWordIdsByUserId(@Param("userId") Long userId);

    List<UserWord> findByUser_UserId(Long userId);
}
