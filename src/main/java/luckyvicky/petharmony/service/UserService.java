package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.user.LogInDTO;
import luckyvicky.petharmony.dto.user.LoginResponseDTO;
import luckyvicky.petharmony.dto.user.SignUpDTO;
import luckyvicky.petharmony.entity.User;

public interface UserService {
    // 회원가입
    User signUp(SignUpDTO signUpDTO);
    // 로그인
    LoginResponseDTO login(LogInDTO logInDTO);
}
