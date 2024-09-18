package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.user.KakaoInfoDTO;
import luckyvicky.petharmony.dto.user.LogInDTO;
import luckyvicky.petharmony.dto.user.LogInResponseDTO;

public interface AuthService {
    // 자체 로그인 메서드
    LogInResponseDTO login(LogInDTO logInDTO);
    // 카카오 로그인 메서드
    LogInResponseDTO kakaoLogin(String accessToken);
    // 카카오 사용자 정보 조회 메서드
    KakaoInfoDTO getUserInfoFromKakao(String accessToken);
}
