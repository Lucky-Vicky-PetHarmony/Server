package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.board.BoardPinResponseDTO;

public interface BoardPinService {

    BoardPinResponseDTO boardPinned(String pinStatus, Long userId, Long boardId);
}
