package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.board.*;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.entity.board.*;
import luckyvicky.petharmony.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

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
    private final BoardPinRepository boardPinRepository;


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
        return getBoardDetailResponseDTO(userId, board);
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

        return getBoardDetailResponseDTO(board.getUser().getUserId(), board);
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
            // 댓글 삭제
            commentRepository.deleteByBoard_BoardId(boardId);

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
     * 게시물 상세페이지
     *
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
        }

        return getBoardDetailResponseDTO(userId, board);
    }

    /**
     * 게시물 리스트 조회
     */
    @Override
    public Page<BoardListResponseDTO> boardList(String category, String sortBy, int page, int size) {

        //정렬
        Sort sort = getSortBy(sortBy);

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Board> boardPage;

        // 검색 및 카테고리 필터링 적용
        if (Objects.equals(category, "ALL")) {
            boardPage = boardRepository.findAll(pageable);
        } else {
            boardPage = boardRepository.findByCategory(Category.valueOf(category), pageable);
        }

        return boardPage.map(this::buildBoardListResponseDTO);
    }

    /**
     * @param keyword 검색어
     * @param searchType 검색타입
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<BoardListResponseDTO> boardSearch(String category, String sortBy, String keyword, String searchType, int page, int size) {

        //정렬
        Sort sort = getSortBy(sortBy);

        // Pageable 객체 생성
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<Board> boards;

        // 검색 및 카테고리 필터링 적용
        if (Objects.equals(category, "ALL")) {
            if ("title".equals(searchType)) {
                boards = boardRepository.findByBoardTitleContainingIgnoreCase(keyword, pageRequest);
            } else if ("content".equals(searchType)) {
                boards = boardRepository.findByBoardContentContainingIgnoreCase(keyword, pageRequest);
            } else {
                boards = boardRepository.findByBoardTitleContainingIgnoreCaseOrBoardContentContainingIgnoreCase(
                        keyword, keyword, pageRequest);
            }
        } else {
            Category categoryEnum = Category.valueOf(category);
            if ("title".equals(searchType)) {
                boards = boardRepository.findByCategoryAndBoardTitleContainingIgnoreCase(categoryEnum, keyword, pageRequest);
            } else if ("content".equals(searchType)) {
                boards = boardRepository.findByCategoryAndBoardContentContainingIgnoreCase(categoryEnum, keyword, pageRequest);
            } else {
                boards = boardRepository.findByCategoryAndBoardTitleContainingIgnoreCaseOrBoardContentContainingIgnoreCase(
                        categoryEnum, keyword, keyword, pageRequest);
            }
        }

        return boards.map(this::buildBoardListResponseDTO);
    }

    /**
     * Board 엔티티를 BoardListResponseDTO로 변환하는 메서드
     *
     * @param board
     * @return
     */
    private BoardListResponseDTO buildBoardListResponseDTO(Board board) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 이미지 유무 조회
        boolean hasImage = imageRepository.existsByBoard_BoardId(board.getBoardId());

        return BoardListResponseDTO.builder()
                .boardId(board.getBoardId())
                .boardTitle(board.getBoardTitle())
                .category(board.getCategory())
                .viewCount(board.getView())
                .commentCount(board.getCommentCount())
                .image(hasImage)
                .boardCreate(board.getBoardCreate().format(formatter))
                .boardUpdate(board.getBoardUpdate().format(formatter))
                .pinCount(board.getPinCount())
                .build();
    }

    // 정렬 방식 설정(filter)
    private Sort getSortBy(String sortBy) {
        if ("comments".equalsIgnoreCase(sortBy)) {
            return Sort.by(Sort.Direction.DESC, "commentCount"); //댓글순
        } else if ("views".equalsIgnoreCase(sortBy)) {
            return Sort.by(Sort.Direction.DESC, "view"); //조회순
        } else if ("pin".equalsIgnoreCase(sortBy)) {
            return Sort.by(Sort.Direction.DESC, "pinCount"); //좋아요순
        } else {
            return Sort.by(Sort.Direction.DESC, "boardUpdate"); // 기본 정렬: 최신순
        }
    }


    // board엔티티를 클라이언트 응답할 DTO로 변환
    private BoardDetailResponseDTO getBoardDetailResponseDTO(Long userId, Board board) {
        //조회하는 사람이 게시물에 좋아요 눌렀는지 여부
        BoardPin boardPin = boardPinRepository.findByBoard_BoardIdAndUser_UserId(board.getBoardId(), userId).orElse(null);

        User user = board.getUser(); //게시글 작성자
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<Image> images = imageRepository.findByBoard_BoardId(board.getBoardId());

        return BoardDetailResponseDTO.builder()
                .boardId(board.getBoardId())
                .userId(board.getUser().getUserId())
                .userName(user.getUserName())
                .title(board.getBoardTitle())
                .content(board.getBoardContent())
                .category(board.getCategory())
                .createTime(board.getBoardCreate().format(formatter))
                .updateTime(board.getBoardUpdate().format(formatter))
                .views(board.getView())
                .images(images)
                .pinStatus(boardPin != null)
                .pinCount(board.getPinCount())
                .build();
    }
}
