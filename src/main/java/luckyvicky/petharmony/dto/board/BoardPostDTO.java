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

    // 게시글 작성한 user id
    private Long userId;

    // 게시글 제목
    private String title;

    // 게시글 내용
    private String content;

    // 카테고리
    private Category category;

    // 첨부파일들
    private List<MultipartFile> images = new ArrayList<>();  // 기본값을 빈 리스트로 설정;
}