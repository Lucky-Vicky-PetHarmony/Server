package luckyvicky.petharmony.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pet_info_word")
public class PetInfoWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "petinfoword_id")
    private Long petInfoWordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_info")
    private PetInfo petInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    private Word word;
}
