package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.comment.CommentPostDTO;
import luckyvicky.petharmony.dto.comment.CommentUpdateDTO;

public interface CommentService {

    //댓글 작성
    void addComment(CommentPostDTO commentPostDTO);

    //댓글 수정
    void updateComment(CommentUpdateDTO commentUpdateDTO);

    //댓글 삭제
}
