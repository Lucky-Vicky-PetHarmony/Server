package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {
    // 특정 word_id 리스트에 해당하는 단어들을 조회하는 메서드
    List<Word> findByWordIdIn(List<Long> wordIds);

    @Query("SELECT w FROM Word w " +
            "WHERE w.wordId IN :wordIds")
    List<Word> findAllByIdsFetch(@Param("wordIds") List<Long> wordIds);

    @Query("SELECT w FROM Word w WHERE w.wordId = :wordId")
    Optional<Word> findByIdFetch(@Param("wordId") Long wordId);

}
