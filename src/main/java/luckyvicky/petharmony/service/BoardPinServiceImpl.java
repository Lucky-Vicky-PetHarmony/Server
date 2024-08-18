package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.entity.board.Board;
import luckyvicky.petharmony.entity.board.BoardPin;
import luckyvicky.petharmony.repository.BoardPinRepository;
import luckyvicky.petharmony.repository.BoardRepository;
import luckyvicky.petharmony.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardPinServiceImpl implements BoardPinService {

    private final BoardPinRepository boardPinRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    /**
     * 게시물 좋아요, 좋아요 취소
     *
     * @param pinStatus 좋아요 상태
     * @param userId 좋아요 누른 유저
     * @param boardId 좋아요 당한 게시물
     */
    @Override
    public String boardPinned(String pinStatus, Long userId, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("유효하지않은 user"));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유효하지않은 board"));

        // 게시물 좋아요 등록
        if(Objects.equals(pinStatus, "like") && board != null && user != null) {
            BoardPin boardPin = BoardPin.builder()
                    .board(board)
                    .user(user)
                    .build();
            boardPinRepository.save(boardPin);
            return "Pin Registration";
        }
        // 게시물 좋아요 취소
        else if (Objects.equals(pinStatus, "unlike") && board != null && user != null) {
            BoardPin boardPin = boardPinRepository.findByBoard_BoardIdAndUser_UserId(boardId, userId).orElseThrow(() -> new IllegalArgumentException("유효하지않은 boardPin"));
            if(boardPin != null) {
                boardPinRepository.delete(boardPin);
            }
            return "Pin Canceled";
        }else {
            return "Board Pinning Error";
        }
    }
}
