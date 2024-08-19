package luckyvicky.petharmony.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckyvicky.petharmony.dto.comment.CommentResponseDTO;
import luckyvicky.petharmony.entity.board.Category;
import luckyvicky.petharmony.entity.board.Image;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDetailResponseDTO {
    private Long boardId;

    private Long userId; //작성자

    private String userName; //작성자 이름

    private String title; //제목

    private String content; //내용

    private Category category; //카테고리

    private String createTime; //생성시간

    private String updateTime; //작성시간

    private Integer views; //조회수

    //이미지
    private List<Image> images = new ArrayList<>(); //빈리스트

    private Integer pinCount; //pin

    //좋아요 여부
    private Boolean pinStatus = Boolean.FALSE;
}
