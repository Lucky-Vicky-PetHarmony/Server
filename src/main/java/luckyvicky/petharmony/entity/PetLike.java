package luckyvicky.petharmony.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pet_like")
public class PetLike implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long likeId;  // 기본 키 컬럼

    @Column(name = "desertion_no", length = 15)
    private String desertionNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 특정 메서드를 통해서만 설정하도록 합니다.
    public void assignUserAndDesertionNo(User user, String desertionNo) {
        this.user = user;
        this.desertionNo = desertionNo;
    }
}

