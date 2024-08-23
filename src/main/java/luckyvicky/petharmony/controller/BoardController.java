package luckyvicky.petharmony.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.board.*;
import luckyvicky.petharmony.service.BoardPinService;
import luckyvicky.petharmony.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/user/board")
public class BoardController {

    private final BoardService boardService;
    private final BoardPinService boardPinService;

    /**
     * 게시글 작성
     *
     * @param boardPostDTO 게시글 정보를 담고 있는 DTO (제목, 내용, 카테고리, 작성자 ID, 첨부파일을 포함)
     * @return 생성된 게시글의 ID를 반환하며, 성공 시 HTTP 200 OK 응답을, 실패 시 HTTP 400 Bad Request 응답을 반환
     * @throws IOException 파일 업로드 중 발생할 수 있는 예외를 처리
     */
    @PostMapping("/post")
    public ResponseEntity<BoardDetailResponseDTO> boardPost(@ModelAttribute BoardPostDTO boardPostDTO) throws IOException {

        BoardDetailResponseDTO boardDetailResponseDTO = boardService.boardPost(boardPostDTO);
        if (boardDetailResponseDTO!=null){
            return ResponseEntity.ok(boardDetailResponseDTO);
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
    public ResponseEntity<BoardDetailResponseDTO> boardUpdate(@ModelAttribute BoardUpdateDTO boardUpdateDTO) throws IOException {

        BoardDetailResponseDTO boardDetailResponseDTO = boardService.boardUpdate(boardUpdateDTO);
        if (boardDetailResponseDTO!=null){
            return ResponseEntity.ok(boardDetailResponseDTO);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    /**
     * 게시글 삭제
     *
     * @param boardDeleteDTO 삭제를 요청한 게시글 작성자 id, 삭제를 요청당한 게시글 id
     * @return 삭제성공시 200반환
     * @throws IOException 파일 삭제 중 발생할 수 있는 예외를 처리
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> boardDelete(@RequestParam Long userId, @RequestParam Long boardId) throws IOException {
        boardService.boardDelete(userId, boardId);

        return ResponseEntity.ok(boardId+"번 게시글 삭제 성공");
    }

    /**
     * 게시글 상세 조회
     *
     * @param
     * @return
     * @throws
     */
    @GetMapping("/view")
    public ResponseEntity<BoardDetailResponseDTO> boardDetailView(@RequestParam Long userId,
                                                                  @RequestParam Long boardId) throws IOException {
        BoardDetailResponseDTO boardDetailResponseDTO = boardService.boardDetail(userId, boardId);
        return ResponseEntity.ok(boardDetailResponseDTO);
    }

    /**
     * 게시글 조회 - 최신순, 댓글순, 조회순/ 카테고리별
     *
     * @param
     * @return
     * @throws
     */
    @GetMapping("/list")
    public Page<BoardListResponseDTO> boardList(@RequestParam String category,
                                                @RequestParam String sortBy,
                                                @RequestParam int page,
                                                @RequestParam int size){

        return boardService.boardList(category, sortBy, page, size);
    }

    /**
     * 게시글 검색 - 제목, 내용/ 검색어
     *
     * @param
     * @return
     * @throws
     */
    @GetMapping("/search")
    public Page<BoardListResponseDTO> boardSearch(@RequestParam String category,
                                                  @RequestParam String sortBy,
                                                  @RequestParam String keyword,
                                                  @RequestParam String searchType, // "title" 또는 "content"
                                                  @RequestParam int page,
                                                  @RequestParam int size){

        return boardService.boardSearch(category,sortBy, keyword, searchType, page, size);
    }

    /**
     * 게시글 좋아요 - 상태에 따라 취소/등록
     *
     * @param boardPinDTO 사용자가 좋아요를 누른 상태와 게시물 및 사용자 정보를 담고 있는 DTO
     * @return 상태에 따른 처리 결과 메시지 (예: "좋아요가 등록되었습니다.", "좋아요가 취소되었습니다.")
     * @throws IOException 입출력 예외 발생 시 던지는 예외
     */
    @PostMapping("/pinned")
    public ResponseEntity<BoardPinResponseDTO> boardPinned(@RequestBody BoardPinDTO boardPinDTO) throws IOException {

        BoardPinResponseDTO boardPinResponseDTO = boardPinService.boardPinned(boardPinDTO.getPinAction(),
                                                         boardPinDTO.getUserId(),
                                                         boardPinDTO.getBoardId());
        if(boardPinResponseDTO!=null){
            return ResponseEntity.ok(boardPinResponseDTO);
        }else {
            return ResponseEntity.badRequest().build();
        }
    }
}
