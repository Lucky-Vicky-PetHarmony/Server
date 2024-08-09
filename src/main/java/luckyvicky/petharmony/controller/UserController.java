package luckyvicky.petharmony.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.user.LogInDTO;
import luckyvicky.petharmony.dto.user.LoginResponseDTO;
import luckyvicky.petharmony.dto.user.SignUpDTO;
import luckyvicky.petharmony.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class UserController {
    private final UserService userService;

    /**
     * 회원가입 API 엔드 포인트
     *
     * @param  signUpDTO
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
     * @return LoginResponseDTO(jwtToken, userName(email), role)
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
}
