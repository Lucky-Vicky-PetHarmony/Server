package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.board.BoardDetailResponseDTO;
import luckyvicky.petharmony.dto.board.BoardPostDTO;
import luckyvicky.petharmony.dto.board.BoardUpdateDTO;
import luckyvicky.petharmony.dto.comment.CommentResponseDTO;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.entity.board.Board;
import luckyvicky.petharmony.entity.board.Comment;
import luckyvicky.petharmony.entity.board.Image;
import luckyvicky.petharmony.repository.BoardRepository;
import luckyvicky.petharmony.repository.CommentRepository;
import luckyvicky.petharmony.repository.ImageRepository;
import luckyvicky.petharmony.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;

    private final CommentService commentService;


    /**
     * 게시물 작성
     *
     * @param boardPostDTO 제목, 내용, 카테고리, 작성자 ID, 첨부파일을 포함하는 DTO
     * @return BoardId 게시판 번호를 반환
     * @throws IOException 파일 업로드 시 발생할 수 있는 예외
     */
    @Override
    public BoardDetailResponseDTO boardPost(BoardPostDTO boardPostDTO) throws IOException {
        Long userId = Long.valueOf(boardPostDTO.getUserId());
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유효하지않은 user ID"));

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
        return getBoardDetailResponseDTO(board);
    }


    /**
     * 게시물 수정
     *
     * @param boardUpdateDTO 제목, 내용, 카테고리, 작성자 ID, 삭제한 첨부파일, 추가한 첨부파일을 포함하는 DTO
     * @return BoardId 게시판 번호를 반환
     * @throws IOException 파일 업로드 시 발생할 수 있는 예외
     */
    @Override
    public BoardDetailResponseDTO boardUpdate(BoardUpdateDTO boardUpdateDTO) throws IOException {
        Board board = boardRepository.findById(boardUpdateDTO.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지않은 board ID"));

        if(!board.getUser().getUserId().equals(boardUpdateDTO.getUserId())){
            throw new IllegalArgumentException("게시글 작성자만 수정 가능합니다.");
        }

        board.updateBoard(boardUpdateDTO.getContent(), boardUpdateDTO.getTitle(), boardUpdateDTO.getCategory());
        boardRepository.flush();

        // 삭제한 이미지가 있을 경우
        if (boardUpdateDTO.getDeleteImages() != null && !boardUpdateDTO.getDeleteImages().isEmpty()) {
            imageService.deleteImage(boardUpdateDTO.getDeleteImages());
        }

        // 추가한 이미지 파일이 있을때만 실행
        if (boardUpdateDTO.getUpdateImages() != null && !boardUpdateDTO.getUpdateImages().isEmpty()) {
            imageService.addImage(board, boardUpdateDTO.getUpdateImages());
        }

        return getBoardDetailResponseDTO(board);
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

    /**
     * @param boardId
     * @return
     * @throws IOException
     */
    @Override
    public BoardDetailResponseDTO boardDetail(Long userId, Long boardId) throws IOException {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 boardId"));
        //조회수
        if(!board.getUser().getUserId().equals(userId)) {
            board.viewCount();
            boardRepository.flush();
        }

        return getBoardDetailResponseDTO(board);
    }

    // board엔티티를 클라이언트 응답할 DTO로 변환
    private BoardDetailResponseDTO getBoardDetailResponseDTO(Board board) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<Image> images = imageRepository.findByBoard_BoardId(board.getBoardId());
        List<Comment> comments = commentRepository.findByBoard_BoardId(board.getBoardId());

        // Comment 객체들을 CommentResponseDTO로 변환
        List<CommentResponseDTO> commentResponseDTOList = comments.stream()
                .map(commentService::convertCommentToCommentResponseDTO)
                .toList();

        return BoardDetailResponseDTO.builder()
                .boardId(board.getBoardId())
                .userId(board.getUser().getUserId())
                .title(board.getBoardTitle())
                .content(board.getBoardContent())
                .category(board.getCategory())
                .createTime(board.getBoardCreate().format(formatter))
                .updateTime(board.getBoardUpdate().format(formatter))
                .views(board.getView())
                .commentCount(comments.size())
                .images(images)
                .commentList(commentResponseDTOList)
                .build();
    }
}
