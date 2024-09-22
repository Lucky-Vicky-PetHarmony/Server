package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.board.BoardListResponseDTO;
import luckyvicky.petharmony.dto.main.PetCardResponseDTO;
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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MainServiceImpl implements MainService {

    private final BoardRepository boardRepository;
    private final ImageRepository imageRepository;
    private final PetInfoRepository petInfoRepository;
    private final PetInfoFormatService petInfoFormatService;

    /**
     * 유기동물 슬라이드를 위한 데이터 조회 메서드
     */
    @Override
    public List<SlideResponseDTO> getSlides() {
        LocalDate currentDate = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 18);

        List<PetInfo> randomPetInfos = petInfoRepository.findRandomByNoticeEdtBefore(currentDate, pageable);

        return randomPetInfos.stream()
                .map(this::mapToSlideResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * 유기동물 슬라이드에 필요한 데이터를 변환하는 헬퍼 메서드
     */
    private SlideResponseDTO mapToSlideResponseDTO(PetInfo petInfo) {
        return SlideResponseDTO.builder()
                .desertionNo(petInfo.getDesertionNo())
                .popFile(petInfo.getPopfile())
                .noticeNo(formatNoticeNo(petInfo.getNoticeNo()))
                .sexCd(formatSexCd(petInfo.getSexCd()))
                .age(formatAge(petInfo.getAge()))
                .build();
    }

    /**
     * 유기동물 카드를 위한 데이터 조회 메서드
     */
    @Override
    public List<PetCardResponseDTO> getPetCards(Long userId) {
        LocalDate currentDate = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 6);

        List<PetInfo> randomPetInfos = petInfoRepository.findRandomByNoticeEdtAfter(currentDate, pageable);

        return randomPetInfos.stream()
                .map(petInfo -> mapToPetCardResponseDTO(petInfo, userId))
                .collect(Collectors.toList());
    }

    /**
     * 유기동물 카드에 필요한 데이터를 변환하는 헬퍼 메서드
     */
    private PetCardResponseDTO mapToPetCardResponseDTO(PetInfo petInfo, Long userId) {
        Map<String, Object> processedInfo = petInfoFormatService.processPetInfo(petInfo, userId);

        return PetCardResponseDTO.builder()
                .desertion_no((String) processedInfo.get("desertion_no"))
                .popfile((String) processedInfo.get("popfile"))
                .words((List<String>) processedInfo.get("words"))
                .kind_cd((String) processedInfo.get("kind_cd"))
                .sex_cd((String) processedInfo.get("sex_cd"))
                .age((String) processedInfo.get("age"))
                .weight((String) processedInfo.get("weight"))
                .org_nm((String) processedInfo.get("care_nm"))
                .neuter_yn((String) processedInfo.get("neuter_yn"))
                .pet_like((Boolean) processedInfo.get("pet_like"))
                .build();
    }

    /**
     * 게시물 목록 조회 메서드
     */
    @Override
    public Page<BoardListResponseDTO> getPublicBoards(int size) {
        Pageable pageable = PageRequest.of(0, size);

        Page<Board> boardPage = boardRepository.findAllByIsDeletedFalse(pageable);

        List<BoardListResponseDTO> boardDTOs = boardPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(boardDTOs, pageable, boardPage.getTotalElements());
    }

    /**
     * Board 엔티티를 BoardListResponseDTO로 변환하는 헬퍼 메서드
     */
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
                .commentCount(board.getCommentCount())
                .image(hasImage)
                .pinCount(board.getPinCount())
                .build();
    }

    /**
     * 공지 번호 포맷팅
     */
    private String formatNoticeNo(String noticeNo) {
        String[] parts = noticeNo.split("-");
        if (parts.length >= 2) {
            return parts[0] + " " + parts[1];
        }
        return noticeNo;
    }

    /**
     * 성별 코드 포맷팅
     */
    private String formatSexCd(String sexCd) {
        switch (sexCd) {
            case "M":
                return "남아";
            case "F":
                return "여아";
            default:
                return "X";
        }
    }

    /**
     * 나이 포맷팅
     */
    private String formatAge(String age) {
        if (age != null && age.length() >= 4) {
            return age.substring(0, 4) + "년생";
        } else {
            return "";
        }
    }
}
