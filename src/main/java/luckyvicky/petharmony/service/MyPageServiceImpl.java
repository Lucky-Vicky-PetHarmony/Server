package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.board.BoardListResponseDTO;
import luckyvicky.petharmony.dto.mypage.*;
import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.PetLike;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.entity.board.Board;
import luckyvicky.petharmony.entity.board.BoardPin;
import luckyvicky.petharmony.entity.board.Comment;
import luckyvicky.petharmony.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BoardPinRepository boardPinRepository;
    private final ImageRepository imageRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final PetLikeRepository petLikeRepository;
    private final PetInfoRepository petInfoRepository;
    private final PetInfoFormatService petInfoFormatService;

    /**
     * 현재 인증된 사용자의 프로필 정보를 조회하는 메서드
     */
    @Override
    public MyProfileResponseDTO getMyProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return MyProfileResponseDTO.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .kakaoId(user.getKakaoId())
                .build();
    }

    /**
     * 현재 인증된 사용자의 프로필 정보를 업데이트하는 메서드
     */
    @Override
    @Transactional
    public MyProfileResponseDTO updateMyProfile(MyProfileRequestDTO myProfileRequestDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.updateUserInfo(myProfileRequestDTO);

        userRepository.save(user);

        return MyProfileResponseDTO.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

    /**
     * 현재 인증된 사용자의 비밀번호를 업데이트하는 메서드
     */
    @Override
    @Transactional
    public void updatePassword(PasswordRequestDTO passwordRequestDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (passwordEncoder.matches(passwordRequestDTO.getPrePassword(), user.getPassword())) {
            user.updatePassword(passwordEncoder.encode(passwordRequestDTO.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
        }
    }

    /**
     * 현재 인증된 사용자가 관심 있는 입양동물 목록을 조회하는 메서드
     */
    @Override
    public Page<MyInterestedPetDTO> getMyInterestedPet(Long userId, Pageable pageable) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Page<PetLike> petLikes = petLikeRepository.findByUserUserId(user.getUserId(), pageable);

        return petLikes.map(petLike -> {
            PetInfo petInfo = petInfoRepository.findByDesertionNo(petLike.getDesertionNo());
            if (petInfo == null) {
                throw new IllegalArgumentException("해당 입양동물 정보를 찾을 수 없습니다: " + petLike.getDesertionNo());
            }

            String orgNm = petInfoRepository.findOrgNmByDesertionNo(petLike.getDesertionNo());

            Map<String, Object> processedInfo = petInfoFormatService.processPetInfo(petInfo, user.getUserId());

            return MyInterestedPetDTO.builder()
                    .desertion_no((String) processedInfo.get("desertion_no"))
                    .popfile((String) processedInfo.get("popfile"))
                    .words((List<String>) processedInfo.get("words"))
                    .kind_cd((String) processedInfo.get("kind_cd"))
                    .sex_cd((String) processedInfo.get("sex_cd"))
                    .age((String) processedInfo.get("age"))
                    .weight((String) processedInfo.get("weight"))
                    .org_nm(orgNm != null ? orgNm : "정보 없음")
                    .neuter_yn((String) processedInfo.get("neuter_yn"))
                    .pet_like((Boolean) processedInfo.get("pet_like"))
                    .build();
        });
    }

    /**
     * 현재 인증된 사용자가 PIN한 게시물들을 조회하는 메서드
     */
    @Override
    public List<BoardListResponseDTO> getPinPosts() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<BoardPin> pins = boardPinRepository.findByUser_UserId(user.getUserId());

        if (pins.isEmpty()) {
            return Collections.emptyList();
        }

        return pins.stream()
                .map(pin -> buildBoardListResponseDTO(pin.getBoard()))
                .collect(Collectors.toList());
    }

    private BoardListResponseDTO buildBoardListResponseDTO(Board board) {
        boolean hasImage = imageRepository.existsByBoard_BoardId(board.getBoardId());
        return BoardListResponseDTO.builder()
                .boardId(board.getBoardId())
                .userId(board.getUser().getUserId())
                .boardTitle(board.getBoardTitle())
                .category(board.getCategory())
                .viewCount(board.getView())
                .boardCreate(board.getBoardCreate().toString())
                .boardUpdate(board.getBoardUpdate().toString())
                .commentCount(board.getCommentCount())
                .image(hasImage)
                .pinCount(board.getPinCount())
                .build();
    }

    /**
     * 현재 인증된 사용자가 작성한 게시물들을 조회하는 메서드
     */
    @Override
    public List<BoardListResponseDTO> getMyPosts() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Board> my = boardRepository.findByUser_UserId(user.getUserId());

        if (my.isEmpty()) {
            return Collections.emptyList();
        }

        return my.stream()
                .map(this::buildBoardListResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * 현재 인증된 사용자가 작성한 댓글들을 조회하는 메서드
     */
    @Override
    public List<MyCommentsDTO> getMyComments() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Comment> my = commentRepository.findByUser_UserId(user.getUserId());

        if (my.isEmpty()) {
            return Collections.emptyList();
        }

        return my.stream()
                .map(comment -> MyCommentsDTO.builder()
                        .boardId(comment.getBoard().getBoardId())
                        .category(comment.getBoard().getCategory())
                        .boardTitle(comment.getBoard().getBoardTitle())
                        .image(imageRepository.existsByBoard_BoardId(comment.getBoard().getBoardId()))
                        .viewCount(comment.getBoard().getView())
                        .commentCount(comment.getBoard().getCommentCount())
                        .pinCount(comment.getBoard().getPinCount())
                        .boardUpdate(comment.getBoard().getBoardUpdate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .content(comment.getCommContent())
                        .commId(comment.getCommId())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 현재 인증된 사용자가 회원 탈퇴를 하는 메서드
     */
    @Override
    @Transactional
    public DeleteAccountResponseDTO deleteMyAccount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.activeIsWithdrawal();

        userRepository.save(user);

        return DeleteAccountResponseDTO.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .role(user.getRole().toString())
                .build();
    }
}
