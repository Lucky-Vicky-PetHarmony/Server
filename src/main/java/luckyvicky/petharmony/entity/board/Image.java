package luckyvicky.petharmony.entity.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import luckyvicky.petharmony.entity.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;                // id

    @Column(name = "image_name", length = 2048, nullable = false)
    private String imageName;         // 게시물 파일 이름

    @Column(name = "image_url", length = 2048, nullable = false)
    private String imageUrl;         // 게시물 파일 url

    @Column(name = "image_uuid", length = 2048, nullable = false)
    private String imageUuid;         // 게시물 파일 uuid

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore //직렬화 무시
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;                // 게시판 테이블
}
