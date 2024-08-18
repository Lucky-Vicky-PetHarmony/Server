package luckyvicky.petharmony.service;

public interface BoardPinService {

    String boardPinned(String pinStatus, Long userId, Long boardId);
}
