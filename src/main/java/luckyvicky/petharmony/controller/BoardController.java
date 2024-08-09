package luckyvicky.petharmony.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.board.BoardPostDTO;
import luckyvicky.petharmony.dto.board.BoardUpdateDTO;
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
    public ResponseEntity<Long> boardPost(@ModelAttribute BoardPostDTO boardPostDTO) throws IOException {

        Long boardId = boardService.boardPost(boardPostDTO);
        if (boardId!=null){
            return ResponseEntity.ok(boardId);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 게시글 수정
     *
     * @param boardUpdateDTO 게시글 수정 정보를 담고 있는 DTO
     * @return 수정된 게시글의 ID를 반환하며, 성공 시 HTTP 200 OK 응답을, 실패 시 HTTP 400 Bad Request 응답을 반환
     * @throws IOException 파일 업로드 중 발생할 수 있는 예외를 처리
     */
    @PutMapping("/update")
    public ResponseEntity<Long> boardUpdate(@ModelAttribute BoardUpdateDTO boardUpdateDTO) throws IOException {

        Long boardId = boardService.boardUpdate(boardUpdateDTO);
        if (boardId!=null){
            return ResponseEntity.ok(boardId);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    /**
     * 게시글 삭제
     *
     * @param userId 삭제를 요청한 게시글 작성자 id
     * @param boardId 삭제를 요청당한 게시글 id
     * @return 삭제성공시 200반환
     * @throws IOException 파일 삭제 중 발생할 수 있는 예외를 처리
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> boardDelete(Long userId, Long boardId) throws IOException {
        boardService.boardDelete(userId, boardId);

        return ResponseEntity.ok(boardId+"번 게시글 삭제 성공");
    }

}
