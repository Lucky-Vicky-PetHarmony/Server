package luckyvicky.petharmony.controller;

import lombok.RequiredArgsConstructor;
import luckyvicky.petharmony.dto.board.BoardListResponseDTO;
import luckyvicky.petharmony.dto.main.PetCardResponseDTO;
import luckyvicky.petharmony.dto.main.SlideResponseDTO;
import luckyvicky.petharmony.service.MainService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public")
public class MainController {

    private final MainService mainService;

    /**
     * ----- 이채림
     * 유기동물 슬라이드 목록 조회 API 엔드포인트
     *
     * @return 유기동물 슬라이드 정보 리스트 (SlideResponseDTO)
     */
    @GetMapping("/slides")
    public List<SlideResponseDTO> getPublicSlides() {
        return mainService.getSlides();
    }


    /**
     * ----- 이채림
     * 유기동물 카드 목록 조회 API 엔드포인트
     *
     * @param userId 사용자 ID
     * @return 유기동물 카드 정보 리스트 (PetCardResponseDTO)
     */
    @GetMapping("/petCards/{userId}")
    public List<PetCardResponseDTO> getPublicPetCards(@PathVariable Long userId) {
        return mainService.getPetCards(userId);
    }


    /**
     * ----- 이채림
     * 게시판 목록 조회 API 엔드포인트
     *
     * @param size 한 페이지에 표시할 게시글 수 (기본값: 5)
     * @return 페이징된 게시판 목록 정보 (Page<BoardListResponseDTO>)
     */
    @GetMapping("/boards")
    public Page<BoardListResponseDTO> getPublicBoards(
            @RequestParam(value = "size", defaultValue = "5") int size) {
        return mainService.getPublicBoards(size);
    }
}
