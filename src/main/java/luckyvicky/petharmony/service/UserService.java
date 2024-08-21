package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.user.*;
import luckyvicky.petharmony.entity.User;

public interface UserService {
    // 자체 회원가입 메서드
    User signUp(SignUpDTO signUpDTO);
    // 자체 로그인 메서드
    LogInResponseDTO login(LogInDTO logInDTO);
    // 아이디 찾기를 위한 인증번호 전송 메서드
    String sendingNumberToFindId(FindIdDTO findIdDTO);
    // 아이디 찾기 시 인증번호 확인 메서드
    FindIdResponseDTO checkNumberToFindid(FindIdDTO findIdDTO);
    // 비밀번호 찾기 시 임시 비밀번호 이메일 전송 메서드
    String sendingEmailToFindPassword(FindPasswordDTO findPasswordDTO);
    // 카카오 액세스 토큰을 사용하여 사용자 정보를 가져오는 메서드
    KakaoInfoDTO getUserInfoFromKakao(String accessToken);
    // 카카오 로그인 처리 메서드
    User kakaoLogin(KakaoInfoDTO kakaoInfoDTO);
}
