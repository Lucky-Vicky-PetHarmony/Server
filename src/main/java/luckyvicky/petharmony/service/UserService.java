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
}
