package luckyvicky.petharmony.controller;

import lombok.RequiredArgsConstructor;
import luckyvicky.petharmony.dto.user.*;
import luckyvicky.petharmony.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /**  ----- 이채림
     * 회원가입 API 엔드포인트
     *
     * @param signUpDTO 회원가입 정보를 담은 DTO (이름, 이메일, 비밀번호, 전화번호 등)
     * @return 성공 시 "PetHarmony에 오신걸 환영합니다." 메시지, 실패 시 오류 메시지 반환
     */
    @PostMapping("/public/signUp")
    public ResponseEntity<String> signUp(@RequestBody SignUpDTO signUpDTO) {
        try {
            userService.signUp(signUpDTO);
            return ResponseEntity.ok("🐶PetHarmony에 오신걸 환영합니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 처리 중 오류가 발생했습니다.");
        }
    }


    /**  ----- 이채림
     * 아이디 찾기 시 인증번호 전송 API 엔드포인트
     *
     * @param findIdDTO 사용자의 전화번호 정보를 담은 DTO
     * @return 성공 시 인증번호 전송 성공 메시지, 실패 시 오류 메시지 반환
     */
    @PostMapping("/public/send-certification")
    public ResponseEntity<String> sendingNumberToFindId(@RequestBody FindIdDTO findIdDTO) {
        try {
            String resultMsg = userService.sendingNumberToFindId(findIdDTO);
            return ResponseEntity.ok(resultMsg);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인증번호 전송에 실패하였습니다.");
        }
    }


    /** ----- 이채림
     * 아이디 찾기 시 인증번호 확인 API 엔드포인트
     *
     *
     * @param findIdDTO 사용자의 전화번호와 인증번호 정보를 담은 DTO
     * @return 성공 시 사용자 아이디와 가입 날짜 정보 반환, 실패 시 오류 메시지 반환
     */
    @PostMapping("/public/check-certification")
    public ResponseEntity<FindIdResponseDTO> checkingNumberToFindId(@RequestBody FindIdDTO findIdDTO) {
        try {
            FindIdResponseDTO findIdResponseDTO = userService.checkNumberToFindid(findIdDTO);
            return ResponseEntity.ok(findIdResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /** ----- 이채림
     * 비밀번호 찾기 시 이메일 전송 API 엔드포인트
     *
     * @param findPasswordDTO 사용자의 이메일 정보를 담은 DTO
     * @return 성공 시 임시 비밀번호 전송 결과 메시지, 실패 시 오류 메시지 반환
     */
    @PostMapping("/public/send-email")
    public ResponseEntity<String> sendingEmailToFindPassword(@RequestBody FindPasswordDTO findPasswordDTO) {
        try {
            String resultMsg = userService.sendingEmailToFindPassword(findPasswordDTO);
            return ResponseEntity.ok(resultMsg);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /** ----- 김가은(Gaannini)
     * 사용자 주소 존재 여부 확인 API 엔드포인트
     *
     * @param userId 주소를 은인할 사용자 ID
     * @return 존재하는 경우 해당 주소, 없는 경우 "Empty Address" 반환
     */
    @GetMapping("/user/existaddr/{userId}")
    public ResponseEntity<String> existAddr(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.userAddrExist(userId));
    }
}