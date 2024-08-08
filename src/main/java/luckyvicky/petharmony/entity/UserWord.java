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
    @Column(name = "userword_di")
    private Long userWordId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "word_id")
    private Word word;
}
