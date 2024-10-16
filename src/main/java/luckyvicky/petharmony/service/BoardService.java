package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.board.*;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface BoardService {

    //게시물 작성
    BoardDetailResponseDTO boardPost(BoardPostDTO boardPostDTO) throws IOException;

    //게시물 수정
    BoardDetailResponseDTO boardUpdate(BoardUpdateDTO boardUpdateDTO) throws IOException;

    //게시물 삭제
    void boardDelete(Long userId, Long boardId) throws IOException;

    //게시물상세 조회
    BoardDetailResponseDTO boardDetail(Long userId, Long boardId) throws IOException;

    //게시물리스트 조회
    Page<BoardListResponseDTO> boardList(String category,
                                         String sortBy,
                                         int page,
                                         int size);

    //게시물 검색
    Page<BoardListResponseDTO> boardSearch(String category, String sortBy, String keyword, String searchType, int page, int size);
}