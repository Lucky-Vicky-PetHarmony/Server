package luckyvicky.petharmony.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_word")
public class UserWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userword_id")
    private Long userWordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    private Word word;

    // 사용자와 단어 간의 다대다 관계를 관리하기 위한 중간 테이블
    // User 설정 메서드
    public void assignUser(User user) {
        this.user = user;
    }

    // Word 설정 메서드
    public void assignWord(Word word) {
        this.word = word;
    }
}
