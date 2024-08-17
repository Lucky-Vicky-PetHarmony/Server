package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.user.*;
import luckyvicky.petharmony.entity.User;

public interface UserService {
    // 회원가입
    User signUp(SignUpDTO signUpDTO);
    // 로그인
    LoginResponseDTO login(LogInDTO logInDTO);
    // 아이디 찾기 1 - 인증번호 전송
    String sendingNumberToFindId(FindIdDTO findIdDTO);
    // 아이디 찾기 2 - 인증번호 확인
    FindIdResponseDTO checkNumberToFindid(FindIdDTO findIdDTO);
    // 비밀번호 찾기 (임시 비밀번호 발송)
    String sendingEmailToFindPassword(FindPasswordDTO findPasswordDTO);
    // 카카오 회원 정보 조회
    KakaoInfoDTO getUserInfoFromKakao(String accessToken);
    // 카카오 로그인
    User kakaoLogin(KakaoInfoDTO kakoInfoDTO);
}