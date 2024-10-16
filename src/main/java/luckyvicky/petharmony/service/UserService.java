package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.user.*;
import luckyvicky.petharmony.entity.User;

public interface UserService {
    // 자체 회원가입 메서드
    User signUp(SignUpDTO signUpDTO);
    // 아이디 찾기를 위한 인증번호 전송 메서드
    String sendingNumberToFindId(FindIdDTO findIdDTO);
    // 아이디 찾기 시 인증번호 확인 메서드
    FindIdResponseDTO checkNumberToFindid(FindIdDTO findIdDTO);
    // 비밀번호 찾기 시 임시 비밀번호 이메일 전송 메서드
    String sendingEmailToFindPassword(FindPasswordDTO findPasswordDTO);
    // 활동 정지 해제 메서드
    void releaseBans();
    // 사용자 주소 확인 메서드
    String userAddrExist(Long userId);
    // 사용자 주소를 업데이트 하는 메서드
    void updateUserAddress(UserAddressDTO userAddressDTO);
}
