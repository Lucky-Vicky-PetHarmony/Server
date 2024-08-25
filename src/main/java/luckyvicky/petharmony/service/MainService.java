package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.board.BoardListResponseDTO;
import luckyvicky.petharmony.dto.main.SlideResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MainService {
    // 슬라이드
    List<SlideResponseDTO> getSlides();
    // 오늘의 게시물
    Page<BoardListResponseDTO> getPublicBoards(int size);
}
