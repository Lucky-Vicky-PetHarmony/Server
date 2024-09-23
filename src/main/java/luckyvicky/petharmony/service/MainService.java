package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.board.BoardListResponseDTO;
import luckyvicky.petharmony.dto.main.PetCardResponseDTO;
import luckyvicky.petharmony.dto.main.SlideResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MainService {
    // 유기동물 슬라이드를 위한 데이터 조회 메서드
    List<SlideResponseDTO> getSlides();
    // 유기동물 카드를 위한 데이터 조회 메서드
    List<PetCardResponseDTO> getPetCards(Long userId);
    // 게시물 목록 조회 메서드
    Page<BoardListResponseDTO> getPublicBoards(int size);
}
