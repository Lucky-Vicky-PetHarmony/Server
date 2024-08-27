package luckyvicky.petharmony.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.board.BoardListResponseDTO;
import luckyvicky.petharmony.dto.mypage.*;
import luckyvicky.petharmony.service.MyPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
public class MyPageController {
    private final MyPageService myPageService;

    /**
     * 사용자 프로필 조회 API 엔드포인트
     *
     * @return ResponseEntity<MyProfileResponseDTO> - 사용자 프로필 정보를 담은 DTO
     */
    @GetMapping("/api/user/myProfile")
    public ResponseEntity<MyProfileResponseDTO> getMyProfile() {
        try {
            MyProfileResponseDTO myProfileResponseDTO = myPageService.getMyProfile();
            return ResponseEntity.ok(myProfileResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 사용자 프로필 수정 API 엔드포인트
     *
     * @param myProfileRequestDTO 수정할 프로필 정보를 담은 요청 DTO
     * @return ResponseEntity<MyProfileResponseDTO> - 수정된 사용자 프로필 정보를 담은 DTO
     */
    @PutMapping("/api/user/myProfile")
    public ResponseEntity<MyProfileResponseDTO> updateMyProfile(@RequestBody MyProfileRequestDTO myProfileRequestDTO) {
        try {
            MyProfileResponseDTO myProfileResponseDTO = myPageService.updateMyProfile(myProfileRequestDTO);
            return ResponseEntity.ok(myProfileResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 사용자 비밀번호 수정 API 엔드포인트
     *
     * @param passwordRequestDTO 수정할 비밀번호 정보를 담은 요청 DTO
     * @return ResponseEntity<?> - 성공 여부를 나타내는 응답
     */
    @PutMapping("/api/user/password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordRequestDTO passwordRequestDTO) {
        try {
            myPageService.updatePassword(passwordRequestDTO);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 사용자가 관심있는 입양 동물 조회 API 엔드포인트
     *
     * @return ResponseEntity<List<MyInterestedPetDTO>> - 관심있는 입양 동물 목록을 담은 DTO 리스트
     */
    @GetMapping("/api/user/interestedPets/{userId}")
    public ResponseEntity<List<MyInterestedPetDTO>> getInterestedPets(@PathVariable Long userId) {
        try {
            List<MyInterestedPetDTO> myInterestedPetDTO = myPageService.getMyInterestedPet(userId);
            return ResponseEntity.ok(myInterestedPetDTO);
        } catch (Exception e) {
            log.error("관심있는 입양 동물 조회 중 오류 발생", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 사용자가 PIN한 게시물 조회 API 엔드포인트
     *
     * @return ResponseEntity<List<BoardListResponseDTO>> - PIN한 게시물 목록을 담은 DTO 리스트
     */
    @GetMapping("/api/user/pinPosts")
    public ResponseEntity<List<BoardListResponseDTO>> getPinPosts() {
        try {
            List<BoardListResponseDTO> pinPosts = myPageService.getPinPosts();
            return ResponseEntity.ok(pinPosts);
        } catch (Exception e) {
            log.error("PIN 게시물 조회 중 오류 발생", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 사용자가 작성한 댓글 조회 API 엔드포인트
     *
     * @return ResponseEntity<List<MyCommentsDTO>> - 작성한 댓글 목록을 담은 DTO 리스트
     */
    @GetMapping("/api/user/myComments")
    public ResponseEntity<List<MyCommentsDTO>> getMyComments() {
        try {
            List<MyCommentsDTO> myComments = myPageService.getMyComments();
            return ResponseEntity.ok(myComments);
        } catch (Exception e) {
            log.error("작성한 댓글 조회 중 오류 발생", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 사용자가 작성한 게시물 조회 API 엔드포인트
     *
     * @return ResponseEntity<List<BoardListResponseDTO>> - 작성한 게시물 목록을 담은 DTO 리스트
     */
    @GetMapping("/api/user/myPosts")
    public ResponseEntity<List<BoardListResponseDTO>> getMyPosts() {
        try {
            List<BoardListResponseDTO> myPosts = myPageService.getMyPosts();
            return ResponseEntity.ok(myPosts);
        } catch (Exception e) {
            log.error("작성한 게시물 조회 중 오류 발생", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 사용자가 회원 탈퇴하는 API 엔드포인트
     *
     * @return ResponseEntity<DeleteAccountResponseDTO> - 회원 탈퇴 결과를 담은 DTO
     */
    @PutMapping("/api/user/deleteAccount")
    public ResponseEntity<DeleteAccountResponseDTO> deleteAccount() {
        try {
            DeleteAccountResponseDTO deleteAccountResponseDTO = myPageService.deleteMyAccount();
            return ResponseEntity.ok(deleteAccountResponseDTO);
        } catch (Exception e) {
            log.error("회원 탈퇴 처리 중 오류 발생");
            return ResponseEntity.badRequest().body(null);
        }
    }
}
