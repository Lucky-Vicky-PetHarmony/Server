package luckyvicky.petharmony;

import luckyvicky.petharmony.repository.UserWordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 데이터베이스 사용
public class UserWordRepositoryTest {

    @Autowired
    private UserWordRepository userWordRepository;

    @BeforeEach
    void setUp() {
        // 필요시 테스트 데이터 초기화 코드 추가
    }

    @Test
    public void testFindWordIdsByUserId() {
        // given: 실제 데이터베이스에 있는 user_id = 27의 word_id 리스트를 가져옴
        Long userId = 27L;
        List<Long> wordIds = userWordRepository.findWordIdsByUserId(userId);

        // when: 반환된 word_id 리스트가 예상대로 있는지 확인
        assertThat(wordIds).isNotNull();
        assertThat(wordIds).isNotEmpty();

        // then: 예상한 word_id들이 포함되어 있는지 확인
        assertThat(wordIds).containsExactlyInAnyOrder(1L, 3L, 5L, 20L);
    }
}
