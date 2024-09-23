package luckyvicky.petharmony.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import luckyvicky.petharmony.dto.user.LogInDTO;
import luckyvicky.petharmony.dto.user.LogInResponseDTO;
import luckyvicky.petharmony.security.JwtTokenProvider;
import luckyvicky.petharmony.service.AuthService;
import luckyvicky.petharmony.util.CookieUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtil cookieUtil;

    /**
     * ----- 이채림
     * 로그인 API 엔드포인트
     * <p>
     * 클라이언트에서 전달받은 로그인 정보를 처리하고, JWT Access Token 및 Refresh Token을 발급합니다.
     * Access Token은 응답 헤더에, Refresh Token은 HttpOnly 쿠키에 저장됩니다.
     *
     * @param logInDTO 로그인 정보를 담은 DTO (이메일, 비밀번호)
     * @param response 응답 객체로, 헤더에 Access Token을 포함하고 쿠키에 Refresh Token을 저장합니다.
     * @return 성공 시 LogInResponseDTO 반환, 실패 시 오류 메시지 반환
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LogInDTO logInDTO, HttpServletResponse response) {
        try {
            LogInResponseDTO logInResponseDTO = authService.login(logInDTO);

            response.setHeader("Authorization", "Bearer " + logInResponseDTO.getJwtToken());

            String refreshToken = jwtTokenProvider.generateRefreshToken(
                    new UsernamePasswordAuthenticationToken(
                            logInResponseDTO.getEmail(),
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority(logInResponseDTO.getRole()))
                    )
            );
            response.addCookie(cookieUtil.createRefreshTokenCookie(refreshToken));

            return ResponseEntity.ok(logInResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /**
     * ----- 이채림
     * Access Token 재발급 API 엔드포인트
     * <p>
     * 클라이언트의 요청에서 Refresh Token을 추출하여 유효성 검사를 통과하면 새로운 Access Token을 발급합니다.
     * 발급된 Access Token은 응답 헤더에 추가됩니다.
     *
     * @param request  클라이언트 요청 객체로, 여기에서 Refresh Token을 추출합니다.
     * @param response 응답 객체로, 헤더에 새로운 Access Token을 포함합니다.
     * @return 성공 시 "Access Token이 성공적으로 재발급되었습니다." 메시지, 실패 시 403 오류 메시지 반환
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieUtil.getTokenFromCookies(request, "refreshToken");

        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(403).body("유효하지 않거나 만료된 Refresh Token입니다.");
        }

        String username = jwtTokenProvider.getUsernameFromJWT(refreshToken);
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, null);
        String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);

        response.setHeader("Authorization", "Bearer " + newAccessToken);

        return ResponseEntity.ok("Access Token이 성공적으로 재발급되었습니다.");
    }


    /**
     * ----- 이채림
     * 로그아웃 API 엔드포인트
     * <p>
     * 사용자가 로그아웃 요청 시 Refresh Token을 쿠키에서 삭제합니다.
     *
     * @param response 응답 객체로, 쿠키에서 Refresh Token을 삭제합니다.
     * @return "로그아웃이 성공적으로 처리되었습니다." 메시지 반환
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        response.addCookie(cookieUtil.createExpiredRefreshTokenCookie());

        return ResponseEntity.ok("로그아웃이 성공적으로 처리되었습니다.");
    }


    /**
     * ----- 이채림
     * 카카오 로그인 API 엔드포인트
     * <p>
     * 클라이언트로부터 전달받은 카카오 Access Token을 사용하여 사용자를 인증하고, JWT Access Token 및 Refresh Token을 발급합니다.
     * Access Token은 응답 헤더에, Refresh Token은 HttpOnly 쿠키에 저장됩니다.
     *
     * @param payload  클라이언트로부터 전달받은 카카오 Access Token을 포함한 맵
     * @param response 응답 객체로, 헤더에 Access Token을 포함하고 쿠키에 Refresh Token을 저장합니다.
     * @return 성공 시 LogInResponseDTO 반환, 실패 시 500 오류 메시지 반환
     */
    @PostMapping("/kakao")
    public ResponseEntity<LogInResponseDTO> kakaoLogin(@RequestBody Map<String, String> payload, HttpServletResponse response) {
        try {
            String kakaoAccessToken = payload.get("accessToken");
            LogInResponseDTO logInResponseDTO = authService.kakaoLogin(kakaoAccessToken);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    logInResponseDTO.getEmail(),
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority(logInResponseDTO.getRole()))
            );

            String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

            response.setHeader("Authorization", "Bearer " + newAccessToken);

            response.addCookie(cookieUtil.createRefreshTokenCookie(refreshToken));

            return ResponseEntity.ok(logInResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
