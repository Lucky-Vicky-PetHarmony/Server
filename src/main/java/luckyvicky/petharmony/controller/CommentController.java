package luckyvicky.petharmony.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.comment.CommentPostDTO;
import luckyvicky.petharmony.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
