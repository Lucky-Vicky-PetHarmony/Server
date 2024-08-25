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
import java.util.Collections;
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
    private final PetInfoFormatService petInfoFormatService;

    /**
     * 유기동물 슬라이드 목록을 가져오는 메서드
     *
     * 현재 날짜 이전에 공고된 유기동물 정보를 슬라이드 형태로 반환합니다.
     * - 유기동물 정보를 랜덤으로 섞어서 반환합니다.
     * - 최대 30개의 유기동물 슬라이드 정보를 반환합니다.
     *
     * @return 유기동물 슬라이드에 표시될 정보를 담은 SlideResponseDTO 리스트
     */
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
        // 리스트를 랜덤으로 섞음
        Collections.shuffle(slideResponseDTOList);
        // 30개 이하인 경우에도 안전하게 슬라이스 처리
        int limit = Math.min(slideResponseDTOList.size(), 30);
        // 섞인 리스트에서 최대 30개의 슬라이드만 반환
        List<SlideResponseDTO> limitedSlideResponseDTOList = slideResponseDTOList.subList(0, limit);
        return limitedSlideResponseDTOList;
    }

    /**
     * 유기동물 카드 목록을 가져오는 메서드
     *
     * 현재 날짜 이후에 공고된 유기동물 정보를 카드 형태로 반환합니다.
     * - 유기동물 정보를 랜덤으로 섞어서 반환합니다.
     * - 최대 6개의 유기동물 카드 정보를 반환합니다.
     *
     * @return 유기동물 카드 정보를 담은 PetCardResponseDTO 리스트
     */
    @Override
    public List<PetCardResponseDTO> getPetCards() {
        LocalDate currentDate = LocalDate.now();
        List<PetInfo> listPetinfo = petInfoRepository.findByNoticeEdtAfter(currentDate);

        List<PetCardResponseDTO> petCardResponseDTOList = listPetinfo.stream()
                .map(petInfo -> PetCardResponseDTO.builder()
                        .desertionNo(petInfo.getDesertionNo())
                        .popFile(petInfo.getPopfile())
                        .words(petInfoFormatService.processWords(petInfo.getWords()))
                        .kindCd(formatKindCd(petInfo.getKindCd()))
                        .sexCd(formatSexCd(petInfo.getSexCd()))
                        .age(formatAge(petInfo.getAge())+"년생")
                        .weigth(petInfo.getWeight())
                        .noticeNo(formatNoticeNo(petInfo.getNoticeNo()))
                        .neuterYn(formatNeuterYn(petInfo.getNeuterYn()))
                        .build())
                .collect(Collectors.toList());
        // 리스트를 랜덤으로 섞음
        Collections.shuffle(petCardResponseDTOList);
        // 6개 이하인 경우에도 안전하게 슬라이스 처리
        int limit = Math.min(petCardResponseDTOList.size(), 6);
        // 섞인 리스트에서 최대 6개의 카드만 반환
        List<PetCardResponseDTO> limitedPetCardResponseDTOList = petCardResponseDTOList.subList(0, limit);
        return limitedPetCardResponseDTOList;
    }

    /**
     * 게시판 목록을 가져오는 메서드
     *
     * 삭제되지 않은 게시글들을 페이징 처리하여 반환합니다.
     * - 게시글 목록을 페이지 단위로 가져옵니다.
     * - 각 게시글에 이미지가 포함되어 있는지 여부도 함께 반환합니다.
     *
     * @param size 한 페이지에 표시할 게시글 수
     * @return 게시판 목록 정보를 담은 BoardListResponseDTO의 페이징된 결과
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

    // 포맷팅
    private String formatNoticeNo(String noticeNo) {
        // 예) 서울-종로-2024-00116 -> 서울 종로
        String[] parts = noticeNo.split("-");
        if (parts.length >= 2) {
            return parts[0] + " " + parts[1];
        }
        return noticeNo;
    }

    private String formatKindCd(String kindCd) {
        String[] parts = kindCd.split("]");
        String kind = parts[0].substring(1, parts[0].length());
        return kind;
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

    private String formatNeuterYn(String neuterYn) {
        switch (neuterYn) {
            case "N":
                return "중성화 안됨";
            case "Y":
                return "중성화 완료";
            default:
                return "알 수 없음";
        }
    }

}
