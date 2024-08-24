package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.board.BoardListResponseDTO;
import org.springframework.data.domain.Page;

public interface MainService {
    Page<BoardListResponseDTO> getPublicBoards(int size);
}
