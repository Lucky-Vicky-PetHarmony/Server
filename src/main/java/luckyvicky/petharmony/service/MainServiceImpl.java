package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.board.BoardListResponseDTO;
import luckyvicky.petharmony.entity.board.Board;
import luckyvicky.petharmony.repository.BoardRepository;
import luckyvicky.petharmony.repository.ImageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class MainServiceImpl implements MainService {
    private final BoardRepository boardRepository;
    private final ImageRepository imageRepository;

    @Override
    public Page<BoardListResponseDTO> getPublicBoards(int size) {
        Pageable pageable = PageRequest.of(0, size);
        Page<Board> boardPage = boardRepository.findAllByIsDeletedFalse(pageable);

        List<BoardListResponseDTO> boardDTOs = boardPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(boardDTOs, pageable, boardPage.getTotalElements());
    }

    private BoardListResponseDTO convertToDTO(Board board) {
        boolean hasImage = imageRepository.existsByBoard_BoardId(board.getBoardId());
        return BoardListResponseDTO.builder()
                .boardId(board.getBoardId())
                .userId(board.getUser().getUserId())
                .boardTitle(board.getBoardTitle())
                .category(board.getCategory())
                .viewCount(board.getCommentCount())
                .boardCreate(board.getBoardCreate().toString())
                .boardUpdate(board.getBoardUpdate().toString())
                .commentCount(board.getComments().size())
                .image(hasImage)
                .pinCount(board.getPinCount())
                .build();
    }
}
