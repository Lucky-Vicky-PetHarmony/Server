package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.board.BoardListResponseDTO;
import luckyvicky.petharmony.dto.main.SlideResponseDTO;
import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.board.Board;
import luckyvicky.petharmony.repository.BoardRepository;
import luckyvicky.petharmony.repository.ImageRepository;
import luckyvicky.petharmony.repository.PetInfoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class MainServiceImpl implements MainService {
    private final BoardRepository boardRepository;
    private final ImageRepository imageRepository;
    private final PetInfoRepository petInfoRepository;

    @Override
    public List<SlideResponseDTO> getSlides() {
        LocalDate currentDate = LocalDate.now();
        List<PetInfo> listPetinfo = petInfoRepository.findByNoticeEdtBefore(currentDate);

        List<SlideResponseDTO> slideResponseDTOList = listPetinfo.stream()
                .map(petInfo -> SlideResponseDTO.builder()
                        .desertionNo(petInfo.getDesertionNo())
                        .popFile(petInfo.getPopfile())
                        .noticeNo(formatNoticeNo(petInfo.getNoticeNo()))
                        .sexCd(formatSexCd(petInfo.getSexCd()))
                        .age(formatAge(petInfo.getAge())+"년생")
                        .build())
                .collect(Collectors.toList());
        log.info(slideResponseDTOList);
        return slideResponseDTOList;
    }

    private String formatNoticeNo(String noticeNo) {
        // 예) 서울-종로-2024-00116 -> 서울 종로
        String[] parts = noticeNo.split("-");
        if (parts.length >= 2) {
            return parts[0] + " " + parts[1];
        }
        return noticeNo;
    }

    private String formatSexCd(String sexCd) {
        switch (sexCd) {
            case "M":
                return "남아";
            case "F":
                return "여아";
            default:
                return "성별 추정 어려움";
        }
    }

    private String formatAge(String age) {
        // 앞에 4글자만 가져오기
        if (age != null && age.length() >= 4) {
            return age.substring(0, 4);
        } else {
            return "";
        }
    }

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
