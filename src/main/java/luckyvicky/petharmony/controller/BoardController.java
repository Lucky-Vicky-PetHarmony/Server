package luckyvicky.petharmony.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.board.BoardPostDTO;
import luckyvicky.petharmony.entity.board.Board;
import luckyvicky.petharmony.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/public/board")
public class BoardController {
    private final BoardService boardService;

    /**
     * 게시글 작성
     *
     * @param boardPostDTO 게시글 정보를 담고 있는 DTO (제목, 내용, 카테고리, 작성자 ID, 첨부파일을 포함)
     * @return 생성된 게시글의 ID를 반환하며, 성공 시 HTTP 200 OK 응답을, 실패 시 HTTP 400 Bad Request 응답을 반환
     * @throws IOException 파일 업로드 중 발생할 수 있는 예외를 처리
     */
    @PostMapping("/post")
    public ResponseEntity<Long> post(@ModelAttribute BoardPostDTO boardPostDTO) throws IOException {

        Long boardId = boardService.boardPost(boardPostDTO);
        if (boardId!=null){
            return ResponseEntity.ok(boardId);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
