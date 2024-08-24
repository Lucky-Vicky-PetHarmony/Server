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
     * 회원가입 API 엔드포인트
     *
     * @param signUpDTO 회원가입 정보를 담은 DTO
     * @return 성공 시 "PetHarmony에 오신걸 환영합니다." 메시지, 실패 시 오류 메시지
     */
    @PostMapping("/api/public/signUp")
    public ResponseEntity<String> signUp(@RequestBody SignUpDTO signUpDTO) {
        try {
            userService.signUp(signUpDTO);
            return ResponseEntity.ok("PetHarmony에 오신걸 환영합니다.");
        } catch (IllegalArgumentException e) {
            // 예외 메시지를 클라이언트에 그대로 반환
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 처리 중 오류가 발생했습니다.");
        }
    }

    /**
     * 로그인 API 엔드포인트
     *
     * @param logInDTO 사용자 로그인 정보를 담은 DTO
     * @return 성공 시 사용자 정보와 JWT 토큰을 포함한 LoginResponseDTO, 실패 시 오류 메시지 반환
     */
    @PostMapping("/api/public/login")
    public ResponseEntity<?> login(@RequestBody LogInDTO logInDTO) {
        try {
            LogInResponseDTO logInResponseDTO = userService.login(logInDTO);
            return ResponseEntity.ok(logInResponseDTO);
        } catch (IllegalArgumentException e) {
            // 예외 메시지를 클라이언트에 그대로 반환 (존재하지 않는 계정, 비밀번호 오류 등)
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            // 탈퇴한 계정, 정지상태인 계정, 카카오 회원인 계정
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인 처리 중 오류가 발생했습니다.");
        }
    }

    /**
     * 아이디 찾기 시 인증번호 전송 API 엔드포인트
     *
     * @param findIdDTO 사용자의 전화번호 정보를 담은 DTO
     * @return 성공 시 인증번호 전송 결과 메시지, 실패 시 오류 메시지
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
     * @param findIdDTO 사용자의 전화번호와 인증번호 정보를 담은 DTO
     * @return 성공 시 사용자 아이디(이메일)와 가입 날짜를 포함한 FindIdResponseDTO, 실패 시 오류 메시지
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
     * 비밀번호 찾기 시 이메일 전송 API 엔드포인트
     *
     * @param findPasswordDTO 사용자의 이메일 정보를 담은 DTO
     * @return 성공 시 임시 비밀번호 전송 결과 메시지, 실패 시 오류 메시지
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
     * 카카오 로그인 API 엔드포인트
     *
     * @param payload 클라이언트로부터 전달받은 카카오 액세스 토큰을 포함한 맵
     * @return 성공 시 JWT 토큰과 사용자 정보를 포함한 KakaoLogInResponseDTO, 실패 시 HTTP 500 상태 반환
     */
    @PostMapping("api/public/kakao")
    public ResponseEntity<KakaoLogInResponseDTO> kakaoLogin(@RequestBody Map<String, String> payload) {
        // 액세스 토큰 추출
        String accessToken = payload.get("accessToken");
        KakaoInfoDTO kakoInfoDTO = userService.getUserInfoFromKakao(accessToken);
        // 가져온 카카오 사용자 정보(kakaoInfo)로 해당 사용자가 있는지 확인, 없다면 계정 생성
        User user = userService.kakaoLogin(kakoInfoDTO);
        // JWT 토큰 생성
        String jwtToken = jwtTokenProvider.generateToken(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        "kakao#password"
                )
        );
        // 반환할 KakaoLogInResponseDTO 객체 생성
        KakaoLogInResponseDTO response = new KakaoLogInResponseDTO(
                jwtToken,
                user.getUserId(),
                user.getEmail(),
                user.getUserName(),
                user.getRole().toString()
        );
        return ResponseEntity.ok(response);
    }
}