package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.UserWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWordRepository extends JpaRepository<UserWord, Long> {
    // 커스텀 쿼리 메소드는 여기에 정의
}

