package luckyvicky.petharmony.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "pet_info")
public class PetInfo {

    @Id
    @Column(name = "desertion_no", nullable = false, length = 15)
    private String desertionNo;

    @Column(name = "special_mark", length = 200)
    private String specialMark;

    @Column(name = "age", length = 30)
    private String age;

    @Column(name = "sex_cd", length = 1)
    private char sexCd;

    @ManyToOne
    @JoinColumn(name = "word_id")
    private Word word;

    public void setWord(Word word) {
        this.word = word;
    }

    public Long getWordId() {
        return word != null ? word.getWordId() : null;
    }
}
