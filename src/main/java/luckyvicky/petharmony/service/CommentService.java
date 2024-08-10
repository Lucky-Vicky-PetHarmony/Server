package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.comment.CommentPostDTO;
import luckyvicky.petharmony.dto.comment.CommentResponseDTO;
import luckyvicky.petharmony.dto.comment.CommentUpdateDTO;
import luckyvicky.petharmony.entity.board.Comment;

public interface CommentService {

    //댓글 작성
    CommentResponseDTO addComment(CommentPostDTO commentPostDTO);

    //댓글 수정
    CommentResponseDTO updateComment(CommentUpdateDTO commentUpdateDTO);

    //댓글 삭제
    void deleteComment(Long userId, Long commId);

    //응답을 위한 dto생성
    CommentResponseDTO convertCommentToCommentResponseDTO(Comment comment);

}
