package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.comment.CommentPostDTO;

public interface CommentService {

    //댓글 작성
    void addComment(CommentPostDTO commentPostDTO);

    //댓글 수정

    //댓글 삭제
}
