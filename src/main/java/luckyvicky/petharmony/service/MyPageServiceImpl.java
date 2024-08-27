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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
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
     *
     * 이 메서드는 현재 로그인된 사용자의 이메일을 기반으로 사용자 정보를 조회하여 반환합니다.
     * 조회된 정보는 사용자 프로필 정보로 변환되어 클라이언트에게 반환됩니다.
     *
     * @return MyProfileResponseDTO - 사용자 프로필 정보를 담은 DTO
     * @throws IllegalArgumentException 사용자를 찾을 수 없을 때 발생
     */
    @Override
    public MyProfileResponseDTO getMyProfile() {
        // 현재 로그인된 사용자의 정보를 가져온다(이메일)
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // 사용자 정보 반환
        return MyProfileResponseDTO.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .kakaoId(user.getKakaoId())
                .build();
    }

    /**
     * 현재 인증된 사용자의 프로필 정보를 업데이트하는 메서드
     *
     * 이 메서드는 클라이언트로부터 전달된 프로필 정보를 사용하여 현재 사용자의 프로필을 업데이트합니다.
     * 업데이트된 정보는 저장된 후 클라이언트에게 반환됩니다.
     *
     * @param myProfileRequestDTO 수정할 프로필 정보를 담은 DTO
     * @return MyProfileResponseDTO - 수정된 사용자 프로필 정보를 담은 DTO
     * @throws IllegalArgumentException 사용자를 찾을 수 없을 때 발생
     */
    @Override
    @Transactional
    public MyProfileResponseDTO updateMyProfile(MyProfileRequestDTO myProfileRequestDTO) {
        // 현재 로그인된 사용자의 정보를 가져온다(이메일)
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // 사용자 정보 업데이트
        user.updateUserInfo(myProfileRequestDTO);
        // 변경사항 저장
        userRepository.save(user);
        // 업데이트된 사용자 정보 반환
        return MyProfileResponseDTO.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

    /**
     * 현재 인증된 사용자의 비밀번호를 업데이트하는 메서드
     *
     * 이 메서드는 클라이언트로부터 전달된 기존 비밀번호와 새로운 비밀번호를 사용하여
     * 현재 사용자의 비밀번호를 업데이트합니다. 기존 비밀번호가 맞지 않을 경우 예외가 발생합니다.
     *
     * @param passwordRequestDTO 수정할 비밀번호 정보를 담은 DTO
     * @throws IllegalArgumentException 기존 비밀번호가 맞지 않거나 사용자를 찾을 수 없을 때 발생
     */
    @Override
    @Transactional
    public void updatePassword(PasswordRequestDTO passwordRequestDTO) {
        // 현재 로그인된 사용자의 정보를 가져온다(이메일)
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // 입력된 기존 비밀번호가 현재 저장된 비밀번호와 일치하는지 확인
        if (passwordEncoder.matches(passwordRequestDTO.getPrePassword(), user.getPassword())) {
            // 새로운 비밀번호를 암호화하여 사용자 객체에 설정
            user.updatePassword(passwordEncoder.encode(passwordRequestDTO.getNewPassword()));
            // 변경사항 저장
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
        }
    }

    /**
     * 현재 인증된 사용자가 PIN한 게시물들을 조회하는 메서드
     *
     * 이 메서드는 현재 사용자가 PIN한 게시물들을 조회하여 리스트로 반환합니다.
     * 게시물은 BoardListResponseDTO로 변환되어 반환됩니다.
     *
     * @return List<BoardListResponseDTO> - PIN한 게시물 목록을 담은 DTO 리스트
     * @throws IllegalArgumentException 사용자를 찾을 수 없을 때 발생
     */
    @Override
    public List<BoardListResponseDTO> getPinPosts() {
        // 현재 로그인된 사용자의 정보를 가져온다(이메일)
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // 사용자가 Pin한 게시물 목록을 가져옴
        List<BoardPin> pins = boardPinRepository.findByUser_UserId(user.getUserId());
        // Pin된 게시물 리스트가 비어있는 경우를 처리할 수 있음
        if (pins.isEmpty()) {
            return Collections.emptyList();
        }
        // Board 엔티티를 DTO로 변환하여 반환
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
     *
     * 이 메서드는 현재 사용자가 작성한 게시물들을 조회하여 리스트로 반환합니다.
     * 게시물은 BoardListResponseDTO로 변환되어 반환됩니다.
     *
     * @return List<BoardListResponseDTO> - 작성한 게시물 목록을 담은 DTO 리스트
     * @throws IllegalArgumentException 사용자를 찾을 수 없을 때 발생
     */
 @Override
    public List<BoardListResponseDTO> getMyPosts() {
        // 현재 로그인된 사용자의 정보를 가져온다(이메일)
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // 사용자가 작성한 게시물 목록을 가져옴
        List<Board> my = boardRepository.findByUser_UserId(user.getUserId());
        // Pin된 게시물 리스트가 비어있는 경우를 처리할 수 있음
        if (my.isEmpty()) {
            return Collections.emptyList();
        }
        return my.stream()
                .map(this::buildBoardListResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * 현재 인증된 사용자가 작성한 댓글들을 조회하는 메소드
     *
     * 이 메서드는 현재 사용자가 작성한 댓글들을 조회하여 리스트로 반환합니다.
     * 각 댓글은 MyCommentsDTO로 변환되어 반환됩니다.
     *
     * @return List<MyCommentsDTO> - 작성한 댓글 목록을 담은 DTO 리스트
     * @throws IllegalArgumentException 사용자를 찾을 수 없을 때 발생
     */
    @Override
    public List<MyCommentsDTO> getMyComments() {
        // 현재 로그인된 사용자의 정보를 가져온다(이메일)
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // 사용자가 작성한 댓글 목록을 가져옴
        List<Comment> my = commentRepository.findByUser_UserId(user.getUserId());
        // 댓글 게시물 리스트가 비어있는 경우를 처리할 수 있음
        if (my.isEmpty()) {
            return Collections.emptyList();
        }
        // Comment 엔티티를 MyCommentsDTO로 변환하여 반환
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
     *
     * 이 메서드는 현재 사용자의 계정을 비활성화하여 회원 탈퇴 처리를 합니다.
     * 탈퇴된 사용자 정보는 DeleteAccountResponseDTO로 반환됩니다.
     *
     * @return DeleteAccountResponseDTO - 회원 탈퇴 처리된 사용자 정보를 담은 DTO
     * @throws IllegalArgumentException 사용자를 찾을 수 없을 때 발생
     */
    @Override
    @Transactional
    public DeleteAccountResponseDTO deleteMyAccount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // 회원 탈퇴
        user.activeIsWithdrawal();
        // 저장
        userRepository.save(user);
        // DeleteAccountResponseDTO 반환
        return DeleteAccountResponseDTO.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .role(user.getRole().toString())
                .build();
    }

    /**
     * 현재 인증된 사용자가 회원 탈퇴를 하는 메서드
     *
     * 이 메서드는 현재 사용자의 계정을 비활성화하여 회원 탈퇴 처리를 합니다.
     * 탈퇴된 사용자 정보는 DeleteAccountResponseDTO로 반환됩니다.
     *
     * @return DeleteAccountResponseDTO - 회원 탈퇴 처리된 사용자 정보를 담은 DTO
     * @throws IllegalArgumentException 사용자를 찾을 수 없을 때 발생
     */
    @Override
    public List<MyInterestedPetDTO> getMyInterestedPet() {
        // 현재 인증된 사용자의 이메일을 가져옴
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // 사용자 ID로 PetLike 리스트 조회
        List<PetLike> petLikes = petLikeRepository.findByUserUserId(user.getUserId());
        // 관심있는 입양동물이 없으면 예외 처리
        if (petLikes.isEmpty()) {
            return Collections.emptyList();
        }
        // PetLike를 순회하며 각 입양동물 정보를 조회하고 DTO로 변환
        return petLikes.stream().map(petLike -> {
            PetInfo petInfo = petInfoRepository.findByDesertionNo(petLike.getDesertionNo());
            if (petInfo == null) {
                throw new IllegalArgumentException("해당 입양동물 정보를 찾을 수 없습니다: " + petLike.getDesertionNo());
            }
            // PetInfo를 처리하여 필요한 정보를 추출
            Map<String, Object> processedInfo = petInfoFormatService.processPetInfo(petInfo, user.getUserId());
            return MyInterestedPetDTO.builder()
                    .desertionNo(petInfo.getDesertionNo())
                    .popFile(petInfo.getPopfile())
                    .words((List<String>) processedInfo.get("words"))
                    .kindCd((String) processedInfo.get("kind_cd_detail"))
                    .sexCd((String) processedInfo.get("sex_cd"))
                    .age((String) processedInfo.get("age"))
                    .weight((String) processedInfo.get("weight"))
                    .orgNm((String) processedInfo.get("care_nm"))
                    .neuterYn((String) processedInfo.get("neuter_yn"))
                    .build();
        }).collect(Collectors.toList());
    }
}
