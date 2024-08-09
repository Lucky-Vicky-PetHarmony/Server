package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.user.LogInDTO;
import luckyvicky.petharmony.dto.user.LoginResponseDTO;
import luckyvicky.petharmony.dto.user.SignUpDTO;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.repository.UserRepository;
import luckyvicky.petharmony.security.CustomUserDetails;
import luckyvicky.petharmony.security.JwtTokenProvider;
import luckyvicky.petharmony.security.Role;
import luckyvicky.petharmony.security.UserState;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     *
     * 기본적으로 Role=USER, UserState=ACTIVE
     * @param signUpDTO
     * @return 생성된 사용자 엔티티 객체
     * @throws IllegalArgumentException 중복된 이메일이 존재할 경우 발생
     */
    @Override
    @Transactional
    public User signUp(SignUpDTO signUpDTO) {
        // 이메일 중복 체크
        if (userRepository.findByEmail(signUpDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signUpDTO.getPassword());

        // User 객체 생성 및 저장
        User user = User.builder()
                .userName(signUpDTO.getUserName())
                .email(signUpDTO.getEmail())
                .password(encodedPassword)
                .phone(signUpDTO.getPhone())
                .role(Role.USER)
                .userState(UserState.ACTIVE)
                .build();

        return userRepository.save(user);
    }

    /**
     * 로그인
     *
     * @param logInDTO
     * @return LoginResponseDTO
     */
    @Override
    public LoginResponseDTO login(LogInDTO logInDTO) {
        // 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        logInDTO.getEmail(),
                        logInDTO.getPassword()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // 인증 성공 시 JWT 토큰 생성 및 반환
        String jwtToken = jwtTokenProvider.generateToken(authentication);
        return LoginResponseDTO.builder()
                .jwtToken(jwtToken)
                .email(userDetails.getUsername())
                .userName(userDetails.getUserName())
                .role(authentication.getAuthorities().toString())
                .build();
    }
}
