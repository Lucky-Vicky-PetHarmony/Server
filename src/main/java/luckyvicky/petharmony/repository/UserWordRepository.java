package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.UserWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWordRepository extends JpaRepository<UserWord, Long> {
    List<UserWord> findByUserUserId(Long userId);

    // 특정 user_id에 해당하는 모든 word_id 리스트를 가져오는 쿼리
    @Query("SELECT uw.word.wordId FROM UserWord uw WHERE uw.user.userId = :userId")
    List<Long> findWordIdsByUserId(@Param("userId") Long userId);
}
