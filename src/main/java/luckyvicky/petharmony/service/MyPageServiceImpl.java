package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
import java.util.List;
import java.util.Map;

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
    public Page<BoardListResponseDTO> getPinPosts(Pageable pageable) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Page<BoardPin> pins = boardPinRepository.findByUserUserId(user.getUserId(), pageable);

        if (pins.isEmpty()) {
            return Page.empty();
        }

        return pins.map(pin -> buildBoardListResponseDTO(pin.getBoard()));
    }

    // Board 엔티티를 buildBoardListResponseDTO로 변환하는 헬퍼 메서드
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
     * 현재 인증된 사용자가 작성한 댓글들을 조회하는 메서드
     */
    @Override
    public Page<MyCommentsDTO> getMyComments(Pageable pageable) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Page<Comment> my = commentRepository.findByUserUserId(user.getUserId(), pageable);

        if (my.isEmpty()) {
            return Page.empty();
        }

        return my.map(comment -> MyCommentsDTO.builder()
                .boardId(comment.getBoard().getBoardId())
                .category(comment.getBoard().getCategory())
                .boardTitle(comment.getBoard().getBoardTitle())
                .image(imageRepository.existsByBoard_BoardId(comment.getBoard().getBoardId())) // boolean 값
                .viewCount(comment.getBoard().getView())
                .commentCount(comment.getBoard().getCommentCount())
                .pinCount(comment.getBoard().getPinCount())
                .boardUpdate(comment.getBoard().getBoardUpdate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .content(comment.getCommContent())
                .commId(comment.getCommId())
                .build());
    }


    /**
     * 현재 인증된 사용자가 작성한 게시물들을 조회하는 메서드
     */
    @Override
    public Page<BoardListResponseDTO> getMyPosts(Pageable pageable) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Page<Board> my = boardRepository.findByUserUserId(user.getUserId(), pageable);

        if (my.isEmpty()) {
            return Page.empty();
        }

        return my.map(this::buildBoardListResponseDTO);
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
