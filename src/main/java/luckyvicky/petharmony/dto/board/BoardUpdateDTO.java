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
public class BoardUpdateDTO {

    // 게시글 id
    private Long boardId;

    // 작성자 id
    private Long userId;

    // 게시글 제목
    private String title;

    // 게시글 내용
    private String content;

    // 카테고리
    private Category category;

    // 삭제된 첨부파일 id들
    private List<Long> deleteImages = new ArrayList<>();

    // 새롭게 추가된 첨부파일들
    private List<MultipartFile> updateImages = new ArrayList<>();
}
