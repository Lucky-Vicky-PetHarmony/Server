package luckyvicky.petharmony.controller;

import lombok.RequiredArgsConstructor;
import luckyvicky.petharmony.dto.board.BoardListResponseDTO;
import luckyvicky.petharmony.dto.mypage.*;
import luckyvicky.petharmony.service.MyPageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class MyPageController {

    private final MyPageService myPageService;

    /**
     * ----- 이채림
     * 사용자 프로필 조회 API 엔드포인트
     *
     * @return ResponseEntity<MyProfileResponseDTO> - 사용자 프로필 정보를 담은 DTO
     */
    @GetMapping("/myProfile")
    public ResponseEntity<MyProfileResponseDTO> getMyProfile() {
        try {
            MyProfileResponseDTO myProfileResponseDTO = myPageService.getMyProfile();
            return ResponseEntity.ok(myProfileResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /**
     * ----- 이채림
     * 사용자 프로필 수정 API 엔드포인트
     *
     * @param myProfileRequestDTO 수정할 프로필 정보를 담은 DTO
     * @return ResponseEntity<MyProfileResponseDTO> - 수정된 사용자 프로필 정보를 담은 DTO
     */
    @PutMapping("/myProfile")
    public ResponseEntity<MyProfileResponseDTO> updateMyProfile(@RequestBody MyProfileRequestDTO myProfileRequestDTO) {
        try {
            MyProfileResponseDTO myProfileResponseDTO = myPageService.updateMyProfile(myProfileRequestDTO);
            return ResponseEntity.ok(myProfileResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /**
     * ----- 이채림
     * 사용자 비밀번호 수정 API 엔드포인트
     *
     * @param passwordRequestDTO 수정할 비밀번호 정보를 담은 요청 DTO
     * @return ResponseEntity<?> - 성공 여부를 나타내는 응답
     */
    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordRequestDTO passwordRequestDTO) {
        try {
            myPageService.updatePassword(passwordRequestDTO);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 수정에 실패하였습니다.");
        }
    }


    /**
     * ----- 이채림
     * 사용자가 관심 있는 입양 동물 조회 API 엔드포인트
     *
     * @param userId 사용자의 고유 ID
     * @param page   요청할 페이지 번호 (기본값 0)
     * @param size   페이지당 보여줄 데이터 수 (기본값 4)
     * @return ResponseEntity<Page < MyInterestedPetDTO>> - 페이징 처리된 관심 있는 입양 동물 목록을 담은 DTO
     */
    @GetMapping("/interestedPets/{userId}")
    public ResponseEntity<Page<MyInterestedPetDTO>> getInterestedPets(@PathVariable Long userId, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "4") int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<MyInterestedPetDTO> myInterestedPetDTOPage = myPageService.getMyInterestedPet(userId, pageable);
            return ResponseEntity.ok(myInterestedPetDTOPage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /**
     * ----- 이채림
     * 사용자가 PIN한 게시물 조회 API 엔드포인트
     *
     * @param page 페이지 번호 (기본값 0)
     * @param size 페이지당 데이터 수 (기본값 10)
     * @return ResponseEntity<Page < BoardListResponseDTO>> - 페이징 처리된 PIN한 게시물 목록을 담은 DTO
     */
    @GetMapping("/pinPosts")
    public ResponseEntity<Page<BoardListResponseDTO>> getPinPosts(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<BoardListResponseDTO> pinPosts = myPageService.getPinPosts(pageable);
            return ResponseEntity.ok(pinPosts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /**
     * ----- 이채림
     * 사용자가 작성한 댓글 조회 API 엔드포인트
     *
     * @param page 페이지 번호 (기본값 0)
     * @param size 페이지당 데이터 수 (기본값 5)
     * @return ResponseEntity<Page < MyCommentsDTO>> - 페이징 처리된 작성한 댓글 목록을 담은 DTO
     */
    @GetMapping("/myComments")
    public ResponseEntity<Page<MyCommentsDTO>> getMyComments(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<MyCommentsDTO> myComments = myPageService.getMyComments(pageable);
            return ResponseEntity.ok(myComments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    /**
     * ----- 이채림
     * 사용자가 작성한 게시물 조회 API 엔드포인트
     *
     * @param page 페이지 번호 (기본값 0)
     * @param size 페이지당 데이터 수 (기본값 10)
     * @return ResponseEntity<Page < BoardListResponseDTO>> - 페이징 처리된 작성한 게시물 목록을 담은 DTO
     */
    @GetMapping("/myPosts")
    public ResponseEntity<Page<BoardListResponseDTO>> getMyPosts(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<BoardListResponseDTO> myPosts = myPageService.getMyPosts(pageable);
            return ResponseEntity.ok(myPosts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /**
     * ----- 이채림
     * 사용자가 회원 탈퇴하는 API 엔드포인트
     *
     * @return ResponseEntity<DeleteAccountResponseDTO> - 회원 탈퇴 결과를 담은 DTO
     */
    @PutMapping("/deleteAccount")
    public ResponseEntity<DeleteAccountResponseDTO> deleteAccount() {
        try {
            DeleteAccountResponseDTO deleteAccountResponseDTO = myPageService.deleteMyAccount();
            return ResponseEntity.ok(deleteAccountResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
