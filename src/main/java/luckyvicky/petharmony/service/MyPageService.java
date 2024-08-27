package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.board.BoardListResponseDTO;
import luckyvicky.petharmony.dto.mypage.*;

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
    // 현재 인증된 사용자가 회원탈퇴를 하는 메서드
    DeleteAccountResponseDTO deleteMyAccount();
    // 현재 인증된 사용자가 관심있는 입양 동물 조회하는 메서드
//    List<MyInterestedPetDTO> getMyInterestedPet();
}
