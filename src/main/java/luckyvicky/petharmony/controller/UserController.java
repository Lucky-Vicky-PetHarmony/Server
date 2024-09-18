package luckyvicky.petharmony.controller;

import lombok.RequiredArgsConstructor;
import luckyvicky.petharmony.dto.user.*;
import luckyvicky.petharmony.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**  ----- 이채림
     * 회원가입 API 엔드포인트
     *
     * 클라이언트에서 전달받은 회원가입 정보를 처리하고, 새로운 사용자를 생성합니다.
     * 사용자가 이미 존재하거나 입력된 정보에 오류가 있을 경우, 오류 메시지를 반환합니다.
     *
     * @param signUpDTO 회원가입 정보를 담은 DTO (이름, 이메일, 비밀번호, 전화번호 등)
     * @return 성공 시 "PetHarmony에 오신걸 환영합니다." 메시지, 실패 시 오류 메시지 반환
     */
    @PostMapping("/api/public/signUp")
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
     * 사용자가 입력한 전화번호를 기반으로 회원이 존재하는지 확인하고,
     * 인증번호를 SMS로 발송하여 아이디 찾기 절차를 시작합니다.
     *
     * @param findIdDTO 사용자의 전화번호 정보를 담은 DTO
     * @return 성공 시 인증번호 전송 성공 메시지, 실패 시 오류 메시지 반환
     */
    @PostMapping("/api/public/send-certification")
    public ResponseEntity<String> sendingNumberToFindId(@RequestBody FindIdDTO findIdDTO) {
        try {
            String resultMsg = userService.sendingNumberToFindId(findIdDTO);
            return ResponseEntity.ok(resultMsg);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인증번호 전송에 실패하였습니다.");
        }
    }

    /** ----- 이채림
     * 인증번호 확인 API 엔드포인트
     *
     * 사용자가 입력한 전화번호와 인증번호를 확인하여, 인증번호가 일치할 경우
     * 해당 사용자의 아이디(이메일)와 가입 날짜를 반환합니다.
     *
     * @param findIdDTO 사용자의 전화번호와 인증번호 정보를 담은 DTO
     * @return 성공 시 사용자 아이디와 가입 날짜 정보 반환, 실패 시 오류 메시지 반환
     */
    @PostMapping("/api/public/check-certification")
    public ResponseEntity<FindIdResponseDTO> checkingNumberToFindId(@RequestBody FindIdDTO findIdDTO) {
        try {
            FindIdResponseDTO findIdResponseDTO = userService.checkNumberToFindid(findIdDTO);
            return ResponseEntity.ok(findIdResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /** ----- 이채림
     * 비밀번호 찾기 시 이메일 전송 API 엔드포인트
     *
     * 사용자가 입력한 이메일로 사용자를 조회한 후, 해당 사용자에게
     * 임시 비밀번호를 생성하여 이메일로 발송합니다.
     *
     * @param findPasswordDTO 사용자의 이메일 정보를 담은 DTO
     * @return 성공 시 임시 비밀번호 전송 결과 메시지, 실패 시 오류 메시지 반환
     */
    @PostMapping("/api/public/send-email")
    public ResponseEntity<String> sendingEmailToFindPassword(@RequestBody FindPasswordDTO findPasswordDTO) {
        try {
            String resultMsg = userService.sendingEmailToFindPassword(findPasswordDTO);
            return ResponseEntity.ok(resultMsg);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /** ----- 김가은(Gaannini)
     * 사용자 주소 존재 여부 확인 API 엔드포인트
     *
     * 주어진 사용자 ID에 대해 해당 사용자의 주소 정보가 존재하는지 확인합니다.
     * 존재하면 주소 정보를, 없으면 "Empty Address" 메시지를 반환합니다.
     *
     * @param userId 주소를 은인할 사용자 ID
     * @return 존재하는 경우 해당 주소, 없는 경우 "Empty Address" 반환
     */
    @GetMapping("api/user/existaddr/{userId}")
    public ResponseEntity<String> existAddr(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.userAddrExist(userId));
    }
}