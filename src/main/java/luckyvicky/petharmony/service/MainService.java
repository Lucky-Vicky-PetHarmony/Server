package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.board.BoardListResponseDTO;
import luckyvicky.petharmony.dto.main.PetCardResponseDTO;
import luckyvicky.petharmony.dto.main.SlideResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MainService {
    // 유기동물 슬라이드 목록을 가져오는 메서드
    List<SlideResponseDTO> getSlides();
    // 유기동물 카드 목록을 가져오는 메서드
    List<PetCardResponseDTO> getPetCards();
    // 게시판 목록을 가져오는 메서드
    Page<BoardListResponseDTO> getPublicBoards(int size);
}
