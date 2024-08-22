package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long> {
    // 특정 word_id 리스트에 해당하는 단어들을 조회하는 메서드
    List<Word> findByWordIdIn(List<Long> wordIds);
}
