package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.user.*;
import luckyvicky.petharmony.entity.Certification;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.repository.CertificationRepository;
import luckyvicky.petharmony.repository.UserRepository;
import luckyvicky.petharmony.security.CustomUserDetails;
import luckyvicky.petharmony.security.JwtTokenProvider;
import luckyvicky.petharmony.security.Role;
import luckyvicky.petharmony.security.UserState;
import luckyvicky.petharmony.util.SmsUtil;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final SmsUtil smsUtil;
    private final CertificationRepository certificationRepository;

    /**
     * 회원가입
     * <p>
     * 사용자가 제공한 정보로 새로운 사용자를 생성하고, Role은 기본적으로 USER로 설정됩니다.
     * UserState는 ACTIVE로 설정됩니다.
     *
     * @param signUpDTO 사용자 회원가입 정보가 담긴 DTO
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
     * <p>
     * 주어진 이메일과 비밀번호를 사용하여 사용자를 인증합니다.
     * 인증이 성공하면 JWT 토큰을 생성하여 반환합니다.
     *
     * @param logInDTO 로그인 정보가 담긴 DTO
     * @return LoginResponseDTO 로그인 성공 시 반환되는 JWT 토큰과 사용자 정보
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

    /**
     * 아이디 찾기 1 - 인증번호 전송
     * <p>
     * 주어진 전화번호로 사용자 정보를 확인하고, 인증번호를 생성하여 전송합니다.
     * 인증번호는 SMS를 통해 사용자에게 전송되며, 데이터베이스에 저장됩니다.
     *
     * @param findIdDTO 사용자가 입력한 전화번호 정보가 담긴 DTO
     * @return 인증번호 전송 결과 메시지
     */
    @Override
    @Transactional
    public String sendingNumberToFindId(FindIdDTO findIdDTO) {
        // 사용자 확인
        Optional<User> optionalUser = userRepository.findByPhone(findIdDTO.getPhone());

        if (optionalUser.isPresent()) {
            String certificationNumber = String.format("%04d", (int) (Math.random() * 10000));
            SingleMessageSentResponse response = smsUtil.sendOne(optionalUser.get().getPhone(), certificationNumber);

            if (response != null && response.getStatusCode().equals("2000")) {
                Certification certification = Certification.builder()
                        .phone(findIdDTO.getPhone())
                        .certificationNumber(certificationNumber)
                        .build();

                certificationRepository.save(certification);
                return "인증번호가 전송되었습니다.";
            } else {
                return "인증번호 전송에 실패하였습니다.";
            }
        } else {
            return "가입되지 않은 번호입니다.";
        }
    }

    /**
     * 아이디 찾기 2 - 인증번호 확인
     * <p>
     * 주어진 전화번호와 인증번호를 확인하여 사용자의 아이디(이메일)를 반환합니다.
     * 인증번호가 유효한 경우 사용자의 정보를 반환하며, 그렇지 않으면 오류 메시지를 반환합니다.
     *
     * @param findIdDTO 전화번호 정보를 담고 있는 DTO
     * @return 인증번호 확인 결과 메시지와 사용자 정보를 담은 FindIdResponseDTO
     */
    @Override
    public FindIdResponseDTO checkNumberToFindid(FindIdDTO findIdDTO) {
        Optional<Certification> optionalCertification = certificationRepository.findTopByPhoneOrderByCreateDateDesc(findIdDTO.getPhone());

        if (optionalCertification.isPresent() && optionalCertification.get().getCertificationNumber().equals(findIdDTO.getCertificationNumber())) {
            Optional<User> optionalUser = userRepository.findByPhone(findIdDTO.getPhone());

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                return new FindIdResponseDTO(user.getEmail(), user.getCreateDate(), null);
            } else {
                return new FindIdResponseDTO(null, null, "해당 전화번호로 등록된 사용자가 없습니다.");
            }
        } else {
            return new FindIdResponseDTO(null, null, "인증번호가 틀립니다.");
        }
    }
}
