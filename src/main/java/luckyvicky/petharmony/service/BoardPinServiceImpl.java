package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.board.BoardPinResponseDTO;
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
    public BoardPinResponseDTO boardPinned(String pinStatus, Long userId, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("유효하지않은 user"));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유효하지않은 board"));

        // 게시물 좋아요 등록
        if(Objects.equals(pinStatus, "like") && board != null && user != null) {
            BoardPin boardPin = BoardPin.builder()
                    .board(board)
                    .user(user)
                    .build();
            boardPinRepository.save(boardPin);
            return BoardPinResponseDTO.builder()
                    .boardId(boardPin.getBoard().getBoardId())
                    .pinStatus(true)
                    .build();
        }
        // 게시물 좋아요 취소
        else if (Objects.equals(pinStatus, "unlike") && board != null && user != null) {
            BoardPin boardPin = boardPinRepository.findByBoard_BoardIdAndUser_UserId(boardId, userId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지않은 boardPin"));
            boardPinRepository.delete(boardPin);
            return BoardPinResponseDTO.builder()
                    .boardId(boardPin.getBoard().getBoardId())
                    .pinStatus(false)
                    .build();
        }
        //예외
        else {
            throw new IllegalArgumentException("유효하지 않은 요청입니다: pinStatus = " + pinStatus);
        }
    }
}
