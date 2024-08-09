package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.board.BoardPostDTO;
import luckyvicky.petharmony.dto.board.BoardUpdateDTO;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.entity.board.Board;
import luckyvicky.petharmony.entity.board.Image;
import luckyvicky.petharmony.repository.BoardRepository;
import luckyvicky.petharmony.repository.ImageRepository;
import luckyvicky.petharmony.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;


    /**
     * 게시물 작성
     *
     * @param boardPostDTO 제목, 내용, 카테고리, 작성자 ID, 첨부파일을 포함하는 DTO
     * @return BoardId 게시판 번호를 반환
     * @throws IOException 파일 업로드 시 발생할 수 있는 예외
     */
    @Override
    public Long boardPost(BoardPostDTO boardPostDTO) throws IOException {
        User user = userRepository.findById(boardPostDTO.getUserId()).orElseThrow(() -> new IllegalArgumentException("유효하지않은 user ID"));

        // board 테이블에 저장
        Board board = Board.builder()
                .boardTitle(boardPostDTO.getTitle())
                .boardContent(boardPostDTO.getContent())
                .category(boardPostDTO.getCategory())
                .user(user)
                .build();
        boardRepository.save(board);

        // 이미지 파일이 있을때만 실행
        if (boardPostDTO.getImages() != null && !boardPostDTO.getImages().isEmpty()) {
            imageService.addImage(board, boardPostDTO.getImages());
        }

        return board.getBoardId();
    }


    /**
     * 게시물 수정
     *
     * @param boardUpdateDTO 제목, 내용, 카테고리, 작성자 ID, 삭제한 첨부파일, 추가한 첨부파일을 포함하는 DTO
     * @return BoardId 게시판 번호를 반환
     * @throws IOException 파일 업로드 시 발생할 수 있는 예외
     */
    @Override
    public Long boardUpdate(BoardUpdateDTO boardUpdateDTO) throws IOException {
        Board board = boardRepository.findById(boardUpdateDTO.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지않은 board ID"));
        List<Image> imageList = new ArrayList<>(); //추가된 이미지 배열을 저장

        Board updateBoard = board.toBuilder()
                .boardTitle(boardUpdateDTO.getTitle())
                .boardContent(boardUpdateDTO.getContent())
                .category(boardUpdateDTO.getCategory())
                .build();

        // 삭제한 이미지가 있을 경우
        if (boardUpdateDTO.getDeleteImages() != null && !boardUpdateDTO.getDeleteImages().isEmpty()) {
            imageService.deleteImage(boardUpdateDTO.getDeleteImages());
        }

        // 추가한 이미지 파일이 있을때만 실행
        if (boardUpdateDTO.getUpdateImages() != null && !boardUpdateDTO.getUpdateImages().isEmpty()) {
            imageService.addImage(board, boardUpdateDTO.getUpdateImages());
        }
        boardRepository.save(updateBoard);

        return board.getBoardId();
    }

    /**
     * 게시물 삭제
     *
     * @param userId
     * @param boardId
     * @throws IOException
     */
    @Override
    public void boardDelete(Long userId, Long boardId) throws IOException {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("유효하지않은 boardId"));
        if(board.getUser().getUserId().equals(userId)) {
            // TODO: 댓글 삭제

            //이미지 삭제
            List<Long> imageIds = imageService.findImageIdsByBoardId(boardId);
            imageService.deleteImage(imageIds);

            //게시글 삭제
            boardRepository.delete(board);
        }else {
            throw new IllegalArgumentException("게시글 작성자가 아닙니다.");
        }
    }
}
