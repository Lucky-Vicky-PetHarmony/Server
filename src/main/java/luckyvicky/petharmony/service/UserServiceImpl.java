package luckyvicky.petharmony.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import luckyvicky.petharmony.util.EmailUtil;
import luckyvicky.petharmony.util.SmsUtil;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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
    private final EmailUtil emailUtil;

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

    /**
     * 비밀번호 찾기 - 이메일로 임시 비밀번호 발송
     * <p>
     * 주어진 이메일을 통해 사용자를 찾고, 임시 비밀번호를 생성하여 사용자의 비밀번호를 업데이트합니다.
     * 임시 비밀번호는 이메일로 발송되며, 사용자는 해당 비밀번호로 로그인 후 비밀번호를 재설정해야 합니다.
     * 사용자가 존재하지 않으면 오류 메시지를 반환합니다.
     *
     * @param findPasswordDTO 이메일 정보를 담고 있는 DTO
     * @return 오류 메시지 또는 빈 문자열을 반환
     */
    @Override
    public String sendingEmailToFindPassword(FindPasswordDTO findPasswordDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(findPasswordDTO.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String certificationCode = String.format("%08d", (int) (Math.random() * 100000000));

            user.updatePassword(certificationCode);

            userRepository.save(user);

            String title = "[PetHarmony] 임시 비밀번호 알림";

            String content = String.format(
                    "안녕하세요. PetHarmony 입니다 🐶" +
                            "\n%s님의 임시 비밀번호는 %s입니다." +
                            "\n임시 비밀번호로 로그인 후 꼭 비밀번호를 재설정 해주시길 바랍니다.",
                    user.getUserName(), certificationCode
            );

            emailUtil.sendEmail(user.getEmail(), title, content);
            return "임시 비밀번호가 이메일로 발송되었습니다.";
        } else {
            return "가입되지 않은 이메일입니다.";
        }
    }

    /**
     * 카카오로부터 사용자 정보 조회
     * <p>
     * 주어진 액세스 토큰을 사용하여 카카오 API를 호출하고, 사용자의 정보를 가져옵니다.
     * 응답이 성공적으로 이루어지면 해당 정보를 KakaoInfoDTO 객체로 반환합니다.
     *
     * @param accessToken 카카오 API 호출에 사용할 액세스 토큰
     * @return KakaoInfoDTO 카카오 사용자 정보가 담긴 객체
     * @throws RuntimeException 카카오 API 호출 실패 또는 응답 처리 중 오류 발생 시
     */
    @Override
    public KakaoInfoDTO getUserInfoFromKakao(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<String> response;

        try {
            response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    entity,
                    String.class
            );
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("카카오 API 호출에 실패했습니다: " + e.getMessage());
        }

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("카카오 API 응답 상태가 좋지 않습니다: " + response.getStatusCode());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoInfoDTO userInfo;
        try {
            userInfo = objectMapper.readValue(response.getBody(), KakaoInfoDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("카카오 API 응답을 처리하는 중 오류가 발생했습니다: " + e.getMessage());
        }

        return userInfo;
    }

    /**
     * 카카오 로그인 처리
     * <p>
     * 카카오 사용자 정보(KakaoInfoDTO)를 사용하여 로그인 처리를 합니다.
     * 이미 등록된 카카오 사용자가 있으면 해당 사용자 정보를 반환하고,
     * 그렇지 않으면 새로운 사용자를 생성하여 저장 후 반환합니다.
     *
     * @param kakaoInfoDTO 카카오에서 제공된 사용자 정보가 담긴 DTO
     * @return User 생성되었거나 기존에 존재하는 사용자 엔티티 객체
     */
    @Override
    public User kakaoLogin(KakaoInfoDTO kakaoInfoDTO) {
        KakaoAccountDTO account = kakaoInfoDTO.getKakao_account();

        Optional<User> existingUser = userRepository.findByKakaoId(kakaoInfoDTO.getId());

        if (existingUser.isPresent()) {
            log.info("Kakao ID {}로 이미 등록된 사용자입니다. 로그인 처리를 진행합니다.", kakaoInfoDTO.getId());
            return existingUser.get();
        } else {
            User user = User.builder()
                    .userName(account.getName())
                    .email(account.getEmail())
                    .password("kakao_password")
                    .phone(account.getPhone_number())
                    .role(Role.USER)
                    .userState(UserState.ACTIVE)
                    .kakaoId(kakaoInfoDTO.getId())
                    .build();

            return userRepository.save(user);
        }
    }
}
