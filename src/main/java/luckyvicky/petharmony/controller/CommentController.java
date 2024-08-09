package luckyvicky.petharmony.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.comment.CommentDeleteDTO;
import luckyvicky.petharmony.dto.comment.CommentPostDTO;
import luckyvicky.petharmony.dto.comment.CommentUpdateDTO;
import luckyvicky.petharmony.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/public/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post")
    public ResponseEntity<String> postComment(@RequestBody CommentPostDTO commentPostDTO) {
        commentService.addComment(commentPostDTO);
        return ResponseEntity.ok(commentPostDTO.getBoardId()+"번 게시글에 댓글달림.");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateComment(@RequestBody CommentUpdateDTO commentUpdateDTO) {
        commentService.updateComment(commentUpdateDTO);
        return ResponseEntity.ok("댓글수정완료");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteComment(@RequestBody CommentDeleteDTO commentDeleteDTO) {
        commentService.deleteComment(commentDeleteDTO.getUserId(), commentDeleteDTO.getCommentId());
        return ResponseEntity.ok("댓글삭제완료");
    }
}
