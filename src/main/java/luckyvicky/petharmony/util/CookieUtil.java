package luckyvicky.petharmony.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    // 도메인 설정
    @Value("${cookie.domain:localhost}")
    private String domain;
    // HTTPS 배포 시 true로 설정 필요
    @Value("${cookie.secure:false}")
    private boolean isSecure;
    // Cookie 유효기간: 7일
    @Value("${cookie.max-age:604800}")
    private int maxAge;

    // Cookie에서 특정 이름의 토큰 값을 추출하는 메서드
    public String getTokenFromCookies(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // Refresh Token을 저장하는 Cookie 생성 메서드
    public Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(maxAge);
        refreshTokenCookie.setSecure(isSecure);
        refreshTokenCookie.setDomain(domain);
        return refreshTokenCookie;
    }

    // Refresh Token을 삭제하는 Cookie 생성 메서드
    public Cookie createExpiredRefreshTokenCookie() {
        Cookie expiredCookie = new Cookie("refreshToken", null);
        expiredCookie.setHttpOnly(true);
        expiredCookie.setPath("/");
        expiredCookie.setMaxAge(0);
        expiredCookie.setSecure(isSecure);
        expiredCookie.setDomain(domain);
        return expiredCookie;
    }
}