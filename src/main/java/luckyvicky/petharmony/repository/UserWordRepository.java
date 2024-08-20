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
     * 특정 사용자(userId)에 해당하는 모든 UserWord 엔티티를 가져오는 메서드
     *
     * @param userId 검색할 사용자의 ID
     * @return 해당 사용자의 UserWord 엔티티 리스트
     */
    List<UserWord> findByUserUserId(Long userId);

    /**
     * 특정 user_id에 해당하는 모든 word_id 리스트를 가져오는 쿼리
     * 이 메서드는 사용자가 선택한 단어들에 대한 word_id 값을 반환
     *
     * @param userId 검색할 사용자의 ID
     * @return 해당 사용자가 선택한 word_id의 리스트
     */
    @Query("SELECT uw.word.wordId FROM UserWord uw WHERE uw.user.userId = :userId")
    List<Long> findWordIdsByUserId(@Param("userId") Long userId);
}
