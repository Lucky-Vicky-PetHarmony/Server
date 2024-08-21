package luckyvicky.petharmony.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.user.*;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.security.JwtTokenProvider;
import luckyvicky.petharmony.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입 API 엔드 포인트
     *
     * @param signUpDTO
     * @return 성공 시, "PetHarmony에 오신걸 환영합니다."
     */
    @PostMapping("/api/public/signUp")
    public ResponseEntity<String> signUp(@RequestBody SignUpDTO signUpDTO) {
        try {
            userService.signUp(signUpDTO);
            return ResponseEntity.ok("PetHarmony에 오신걸 환영합니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 로그인 API 엔드 포인트
     *
     * @param logInDTO
     * @return LoginResponseDTO(jwtToken, userName ( email), role)
     */
    @PostMapping("/api/public/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LogInDTO logInDTO) {
        try {
            LoginResponseDTO loginResponseDTO = userService.login(logInDTO);
            return ResponseEntity.ok(loginResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 인증번호 전송 API 엔드 포인트
     *
     * @param findIdDTO
     * @return 메시지 반환(인증번호가 전송되었습니다. || 인증번호 전송에 실패하였습니다. || 가입되지 않은 번호입니다.)
     */
    @PostMapping("/api/public/send-certification")
    public ResponseEntity<String> sendingNumberToFindId(@RequestBody FindIdDTO findIdDTO) {
        try {
            String resultMsg = userService.sendingNumberToFindId(findIdDTO);
            return ResponseEntity.ok(resultMsg);
        } catch (Exception e) {
            e.printStackTrace();  // 예외 스택 트레이스를 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인증번호 전송에 실패하였습니다.");
        }
    }

    /**
     * 인증번호 확인 API 엔드포인트
     *
     * @param findIdDTO 전화번호와 인증번호 정보가 포함된 DTO
     * @return ResponseEntity<FindIdResponseDTO>
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

    /**
     * 임시 비밀번호 발송 API 엔드포인트
     *
     * @param findPasswordDTO
     * @return 메시지 반환
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

    /**
     * 카카오 로그인 API 엔드 포인트
     *
     * @param payload
     * @return KakaoLogInResponseDTO
     */
    @PostMapping("api/public/kakao")
    public ResponseEntity<KakaoLogInResponseDTO> kakaoLogin(@RequestBody Map<String, String> payload) {
        String accessToken = payload.get("accessToken");
        KakaoInfoDTO kakoInfoDTO = userService.getUserInfoFromKakao(accessToken);

        User user = userService.kakaoLogin(kakoInfoDTO);

        String jwtToken = jwtTokenProvider.generateToken(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        "kakao#password"
                )
        );

        KakaoLogInResponseDTO response = new KakaoLogInResponseDTO(
                jwtToken,
                user.getEmail(),
                user.getUserName(),
                user.getRole().toString()
        );

        return ResponseEntity.ok(response);
    }
}