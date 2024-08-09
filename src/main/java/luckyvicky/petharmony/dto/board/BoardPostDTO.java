package luckyvicky.petharmony.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckyvicky.petharmony.entity.board.Category;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardPostDTO {
    private String title;
    private String content;
    private Category category;
    private Long userId;
    private List<MultipartFile> images = new ArrayList<>();  // 기본값을 빈 리스트로 설정;
}