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
@Table(name = "word")
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id")
    private Long wordId;

    @Column(name = "word_select")
    private String wordSelect;

    // 사용자가 선택한 단어 목록은 직접 쿼리를 작성하여 조회할 예정이므로 @OneToMany 제거
}
