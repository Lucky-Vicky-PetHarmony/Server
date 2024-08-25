package luckyvicky.petharmony.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.board.BoardListResponseDTO;
import luckyvicky.petharmony.dto.main.PetCardResponseDTO;
import luckyvicky.petharmony.dto.main.SlideResponseDTO;
import luckyvicky.petharmony.service.MainService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
public class MainController {
    private final MainService mainService;

    /**
     * 유기동물 슬라이드 목록을 가져오는 API 엔드포인트
     *
     * @return 슬라이드에 표시될 유기동물 정보를 담은 SlideResponseDTO 리스트
     */
    @GetMapping("/api/public/slides")
    public List<SlideResponseDTO> getPublicSlides() {
        return mainService.getSlides();
    }

    /**
     * 유기동물 카드 목록을 가져오는 API 엔드포인트
     *
     * @return 유기동물 카드 정보를 담은 PetCardResponseDTO 리스트
     */
    @GetMapping("/api/public/petCards")
    public List<PetCardResponseDTO> getPublicPetCards() {
        return mainService.getPetCards();
    }

    /**
     * 게시판 목록을 가져오는 API 엔드포인트
     *
     * @param size 한 페이지에 표시할 게시글 수 (기본값: 5)
     * @return 게시판 목록 정보를 담은 BoardListResponseDTO의 페이징된 결과
     */
    @GetMapping("/api/public/boards")
    public Page<BoardListResponseDTO> getPublicBoards(@RequestParam(value = "size", defaultValue = "5") int size) {
        return mainService.getPublicBoards(size);
    }
}
