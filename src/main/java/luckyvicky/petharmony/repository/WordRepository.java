package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long> {
    // 커스텀 쿼리 메소드는 여기에 정의
}
