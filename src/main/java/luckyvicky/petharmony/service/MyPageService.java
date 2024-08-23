package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.board.BoardListResponseDTO;
import luckyvicky.petharmony.dto.mypage.MyCommentsDTO;
import luckyvicky.petharmony.dto.mypage.MyProfileRequestDTO;
import luckyvicky.petharmony.dto.mypage.MyProfileResponseDTO;
import luckyvicky.petharmony.dto.mypage.PasswordRequestDTO;

import java.util.List;

public interface MyPageService {
    // 현재 인증된 사용자의 프로필 정보를 조회하는 메서드
    MyProfileResponseDTO getMyProfile();
    // 현재 인증된 사용자의 프로필 정보를 업데이트하는 메서드
    MyProfileResponseDTO updateMyProfile(MyProfileRequestDTO myProfileRequestDTO);
    // 현재 인증된 사용자의 비밀번호를 업데이트하는 메서드
    void updatePassword(PasswordRequestDTO passwordRequestDTO);
    // 현재 인증된 사용자가 PIN한 게시물들을 조회하는 메서드
    List<BoardListResponseDTO> getPinPosts();
    // 현재 인증된 사용자가 작성한 게시물들을 조회하는 메서드
    List<BoardListResponseDTO> getMyPosts();
    // 현재 인증된 사용자가 작성한 댓글들을 조회하는 메서드
    List<MyCommentsDTO> getMyComments();
}
