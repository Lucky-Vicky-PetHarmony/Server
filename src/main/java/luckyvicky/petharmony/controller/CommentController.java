package luckyvicky.petharmony.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.comment.CommentPostDTO;
import luckyvicky.petharmony.dto.comment.CommentResponseDTO;
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
    public ResponseEntity<CommentResponseDTO> postComment(@RequestBody CommentPostDTO commentPostDTO) {
        CommentResponseDTO commentResponseDTO = commentService.addComment(commentPostDTO);
        if (commentResponseDTO == null) {
            return ResponseEntity.badRequest().body(null);
        }else{
            return ResponseEntity.ok(commentResponseDTO);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<CommentResponseDTO> updateComment(@RequestBody CommentUpdateDTO commentUpdateDTO) {
        CommentResponseDTO commentResponseDTO = commentService.updateComment(commentUpdateDTO);
        if (commentResponseDTO == null) {
            return ResponseEntity.badRequest().body(null);
        }else{
            return ResponseEntity.ok(commentResponseDTO);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteComment(@RequestParam Long userId, @RequestParam Long commentId) {
        commentService.deleteComment(userId, commentId);
        return ResponseEntity.ok("댓글삭제완료");
    }
}
