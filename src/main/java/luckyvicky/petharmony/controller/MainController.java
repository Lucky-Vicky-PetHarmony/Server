package luckyvicky.petharmony.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.board.BoardListResponseDTO;
import luckyvicky.petharmony.entity.board.Board;
import luckyvicky.petharmony.service.MainService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class MainController {
    private final MainService mainService;

    @GetMapping("/api/public/boards")
    public Page<BoardListResponseDTO> getPublicBoards(@RequestParam(value = "size", defaultValue = "5") int size) {
        return mainService.getPublicBoards(size);
    }
}
