package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.board.BoardPostDTO;
import luckyvicky.petharmony.dto.board.BoardUpdateDTO;

import java.io.IOException;

public interface BoardService {

    //게시물 작성
    Long boardPost(BoardPostDTO boardPostDTO) throws IOException;

    //게시물 수정
    Long boardUpdate(BoardUpdateDTO boardUpdateDTO) throws IOException;

    //게시물 삭제

    //게시물 조회수 관리

    //댓글 작성

    //댓글 수정
}