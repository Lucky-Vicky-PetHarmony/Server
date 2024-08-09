package luckyvicky.petharmony.repository;

import luckyvicky.petharmony.entity.UserWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWordRepository extends JpaRepository<UserWord, Long> {
    List<UserWord> findByUserUserId(Long userId);
}
