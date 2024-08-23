package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.board.BoardListResponseDTO;
import luckyvicky.petharmony.dto.mypage.MyCommentsDTO;
import luckyvicky.petharmony.dto.mypage.MyProfileRequestDTO;
import luckyvicky.petharmony.dto.mypage.MyProfileResponseDTO;
import luckyvicky.petharmony.dto.mypage.PasswordRequestDTO;
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

    // 현재 인증된 사용자의 프로필 정보를 조회하는 메서드
    @Override
    public MyProfileResponseDTO getMyProfile() {
        // 현재 로그인된 사용자의 정보를 가져온다(이메일)
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        System.out.println("조회된 사용자: " + user);
        // 사용자 정보 반환
        return MyProfileResponseDTO.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

    // 현재 인증된 사용자의 프로필 정보를 업데이트하는 메서드
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

    // 현재 인증된 사용자의 비밀번호를 업데이트하는 메서드
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

    // 현재 인증된 사용자가 PIN한 게시물들을 조회하는 메서드
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        boolean hasImage = imageRepository.existsByBoard_BoardId(board.getBoardId());

        return BoardListResponseDTO.builder()
                .boardId(board.getBoardId())
                .userId(board.getUser().getUserId())
                .boardTitle(board.getBoardTitle())
                .category(board.getCategory())
                .viewCount(board.getView())
                .boardCreate(board.getBoardCreate().format(formatter))
                .boardUpdate(board.getBoardUpdate().format(formatter))
                .commentCount(board.getCommentCount())
                .image(hasImage)
                .pinCount(board.getPinCount())
                .build();
    }

    // 현재 인증된 사용자가 작성한 게시물들을 조회하는 메서드
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

    // 현재 인증된 사용자가 작성한 댓글들을 조회하는 메소드
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
}
