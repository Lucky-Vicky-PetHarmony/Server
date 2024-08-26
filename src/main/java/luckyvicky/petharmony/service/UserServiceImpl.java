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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
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
     * 자체 회원가입 메서드
     *
     * 사용자가 제공한 정보로 새로운 사용자 계정을 생성합니다.
     * - 탈퇴한 사용자 이메일, 카카오 회원인지, 기존 이메일 존재 여부를 확인합니다.
     * - 비밀번호를 암호화하여 저장하고, 기본적으로 사용자 상태를 ACTIVE로 설정합니다.
     *
     * @param signUpDTO 사용자 회원가입 정보가 담긴 DTO
     * @return 생성된 사용자 엔티티 객체
     * @throws IllegalArgumentException 탈퇴한 계정, 카카오 회원, 중복된 이메일일 경우 예외 발생
     */
    @Override
    @Transactional
    public User signUp(SignUpDTO signUpDTO) {
        // 탈퇴한 사용자 이메일인지 확인
        Optional<User> withdrawanUser = userRepository.findByIsWithdrawalTrueAndEmail(signUpDTO.getEmail());
        if (withdrawanUser.isPresent()) {
            throw new IllegalArgumentException("🐶해당 이메일은 탈퇴한 계정입니다." +
                    "\npetharmony77@gmail.com로 문의주세요.");
        }
        // 카카오 회원인지 확인
        Optional<User> kakaoUser = userRepository.findByEmailAndKakaoIdIsNotNull(signUpDTO.getEmail());
        if (kakaoUser.isPresent()) {
            throw new IllegalArgumentException("🐶카카오 로그인으로 회원가입한 사용자입니다." +
                    "\n[카카오로 시작하기]로 로그인을 진행해주세요.");
        }
        // 이미 사용 중인 이메일인지 확인
        Optional<User> existingUser = userRepository.findByEmail(signUpDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("🐶이미 사용 중인 이메일입니다." +
                    "\n다른 이메일로 회원가입을 진행해주세요.");
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
                .isWithdrawal(false)
                .build();

        return userRepository.save(user);
    }

    /**
     * 자체 로그인 메서드
     *
     * 사용자가 제공한 이메일과 비밀번호로 인증을 시도합니다.
     * - 카카오 회원인지, 탈퇴한 계정인지, 활동 정지된 계정인지 확인합니다.
     * - 인증에 성공하면 JWT 토큰을 생성하여 반환합니다.
     *
     * @param logInDTO 로그인 요청 정보가 담긴 DTO
     * @return 인증에 성공한 사용자의 정보와 JWT 토큰을 담은 LoginResponseDTO 객체
     * @throws IllegalArgumentException 존재하지 않는 계정이거나 비밀번호가 틀렸을 경우 예외 발생
     * @throws IllegalStateException 탈퇴한 계정이거나, 정지된 계정, 또는 카카오 회원일 경우 예외 발생
     */
    @Override
    public LogInResponseDTO login(LogInDTO logInDTO) {
        try {
            Optional<User> kakaoUser = userRepository.findByEmailAndKakaoIdIsNotNull(logInDTO.getEmail());
            if (kakaoUser.isPresent()) {
                throw new IllegalStateException("🐶카카오로 회원가입한 사용자입니다." +
                        "\n카카오 로그인으로 진행해주세요.");
            }
            // 사용자 인증
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            logInDTO.getEmail(),
                            logInDTO.getPassword()
                    )
            );
            // 인증된 사용자 정보를 CustomUserDetails로 캐스팅하여 가져옴
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            // 회원탈퇴한 사용자인지 확인 후 에러 처리
            if (userDetails.getIsWithdrawal()) {
                throw new IllegalStateException("🐶탈퇴한 계정입니다." +
                        "\npetharmony77@gmail.com로 문의주세요.");
            }
            // 회원 상태가 BANNED인지 확인
            if (userDetails.getUserState() == UserState.BANNED) {
                throw new IllegalStateException("🐶활동이 정지된 계정입니다."+
                        "\npetharmony77@gmail.com으로 문의주세요.");
            }
            // 인증 성공 시 JWT 토큰 생성 및 LoginResponseDTO 반환
            String jwtToken = jwtTokenProvider.generateToken(authentication);
            return LogInResponseDTO.builder()
                    .jwtToken(jwtToken)
                    .userId(userDetails.getUser().getUserId())
                    .email(userDetails.getUsername())
                    .userName(userDetails.getUserName())
                    .role(authentication.getAuthorities().toString())
                    .build();
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("🐶존재하지 않는 계정입니다." +
                    "\n아이디 찾기나 비밀번호 찾기를 이용해주세요.");
        }
    }

    /**
     * 아이디 찾기를 위한 인증번호 전송 메서드
     *
     * 사용자가 입력한 전화번호로 사용자를 조회하고, 해당 사용자가 존재하면
     * 4자리 랜덤 인증번호를 생성하여 SMS로 전송합니다.
     * SMS 전송이 성공하면 인증번호를 Certification 엔티티로 저장하고,
     * 성공 메시지를 반환합니다. 사용자가 존재하지 않거나 SMS 전송이 실패한 경우
     * 각각의 상황에 맞는 오류 메시지를 반환합니다.
     *
     * @param findIdDTO 사용자의 전화번호 정보가 담긴 DTO
     * @return 인증번호 전송 결과 메시지
     */
    @Override
    @Transactional
    public String sendingNumberToFindId(FindIdDTO findIdDTO) {
        // 전화번호로 사용자 조회
        Optional<User> optionalUser = userRepository.findByPhoneAndKakaoIdIsNull(findIdDTO.getPhone());
        // 사용자가 존재하면 4자리 랜덤 인증번호 생성 -> SMS를 통해 인증번호 전송
        if (optionalUser.isPresent()) {
            String certificationNumber = String.format("%04d", (int) (Math.random() * 10000));
            SingleMessageSentResponse response = smsUtil.sendOne(optionalUser.get().getPhone(), certificationNumber);
            // SMS 전송이 성공하면 인증번호 Certification 엔티티로 저장
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
     * 아이디 찾기 시 인증번호 확인 메서드
     *
     * 사용자가 입력한 전화번호로 최근에 생성된 인증번호(Certification)를 조회하고,
     * 입력한 인증번호와 일치하는지 확인합니다. 인증번호가 일치하면, 해당 전화번호로
     * 등록된 사용자를 조회하여 사용자의 이메일과 가입 날짜를 반환합니다.
     * 일치하지 않거나 사용자가 존재하지 않는 경우, 각각의 상황에 맞는 오류 메시지를 반환합니다.
     *
     * @param findIdDTO 사용자의 전화번호와 인증번호 정보가 담긴 DTO
     * @return 사용자의 이메일, 가입 날짜 또는 오류 메시지를 담은 FindIdResponseDTO 객체
     */
    @Override
    public FindIdResponseDTO checkNumberToFindid(FindIdDTO findIdDTO) {
        // findIdDTO의 전화번호를 사용해, 해당 번호로 가장 최근에 생성된 Certification 객체를 조회
        Optional<Certification> optionalCertification = certificationRepository.findTopByPhoneOrderByCreateDateDesc(findIdDTO.getPhone());
        // Certification 객체가 존재하고, 그 인증번호가 사용자가 입력한 인증번호와 일치하는지 확인
        if (optionalCertification.isPresent() && optionalCertification.get().getCertificationNumber().equals(findIdDTO.getCertificationNumber())) {
            // 전화번호를 사용해 사용자 조회
            Optional<User> optionalUser = userRepository.findByPhoneAndKakaoIdIsNull(findIdDTO.getPhone());
            // 사용자가 존재하면 User 객체를 가져와 이메일과 가입날짜를 반환하는 FindIdResponseDTO 객체 반환
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
     * 비밀번호 찾기 시 임시 비밀번호 이메일 전송 메서드
     *
     * 사용자가 입력한 이메일로 사용자를 조회한 후, 해당 사용자가 카카오톡으로 로그인한
     * 사용자라면, 그에 맞는 메시지를 반환합니다. 그렇지 않은 경우, 8자리의 랜덤 임시
     * 비밀번호를 생성하여 암호화한 후, 사용자 계정의 비밀번호를 업데이트하고,
     * 해당 임시 비밀번호를 사용자의 이메일로 전송합니다.
     *
     * @param findPasswordDTO 사용자의 이메일 정보가 담긴 DTO
     * @return 임시 비밀번호 전송 결과 메시지
     */
    @Override
    public String sendingEmailToFindPassword(FindPasswordDTO findPasswordDTO) {
        // findPasswordDTO의 이메일을 사용해 사용자 조회
        Optional<User> optionalUser = userRepository.findByEmail(findPasswordDTO.getEmail());
        // 카카오톡으로 로그인한 사용자인지 확인
        if (optionalUser.isPresent() && optionalUser.get().getKakaoId() != null) {
            return "카카오톡으로 로그인한 사용자입니다.";
        }
        // 사용자가 존재하면, 임시 비밀번호로 사용할 8자리의 랜덤 숫자 생성
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String certificationCode = String.format("%08d", (int) (Math.random() * 100000000));
            // 생성된 임시 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(certificationCode);
            // User 객체 password 업데이트
            user.updatePassword(encodedPassword);
            // 변경된 사용자 정보 DB에 저장
            userRepository.save(user);
            // 이메일 제목
            String title = "[PetHarmony] 임시 비밀번호 알림";
            // 이메일 내용
            String content = String.format(
                    "안녕하세요. PetHarmony 입니다 🐶" +
                            "\n%s님의 임시 비밀번호는 %s입니다." +
                            "\n임시 비밀번호로 로그인 후 꼭 비밀번호를 재설정 해주시길 바랍니다.",
                    user.getUserName(), certificationCode
            );
            // 작성된 이메일을 사용자의 이메일로 전송
            emailUtil.sendEmail(user.getEmail(), title, content);
            return "임시 비밀번호가 이메일로 발송되었습니다.";
        } else {
            return "가입되지 않은 이메일입니다.";
        }
    }

    /**
     * 카카오 액세스 토큰을 사용하여 사용자 정보를 가져오는 메서드
     *
     * 주어진 액세스 토큰을 이용해 카카오 API를 호출하여 사용자 정보를 가져옵니다.
     * HTTP 요청을 위해 RestTemplate과 HttpHeaders를 설정하고, 카카오 API로부터
     * 사용자 정보를 받아온 후, 이를 KakaoInfoDTO 객체로 변환하여 반환합니다.
     * API 호출이 실패하거나 응답 처리 중 오류가 발생할 경우, 예외를 발생시킵니다.
     *
     * @param accessToken 카카오로부터 발급받은 액세스 토큰
     * @return 카카오 사용자 정보가 담긴 KakaoInfoDTO 객체
     * @throws RuntimeException 카카오 API 호출 실패 또는 응답 처리 중 오류가 발생한 경우
     */
    @Override
    public KakaoInfoDTO getUserInfoFromKakao(String accessToken) {
        // RestTemplate 객체를 생성하여 HTTP 요청 준비
        RestTemplate restTemplate = new RestTemplate();
        // HttpHeaders 객체를 생성하고, 액세스 토큰을 포함한 Authorization 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        // HttpEntity 객체를 생성하여 요청에 필요한 헤더 정보를 포함
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<String> response;
        // 카카오 API를 호출하여 사용자 정보를 가져온다.
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
        // API 응답이 OK(200)이 아닌 경우, 예외 발생
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("카카오 API 응답 상태가 좋지 않습니다: " + response.getStatusCode());
        }
        // ObjectMapper를 사용해 응답을 KakoInfoDTO 객체로 변환
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
     * 카카오 로그인 처리 메서드
     *
     * 카카오에서 가져온 사용자 정보를 기반으로 로그인 처리를 수행합니다.
     * 먼저, 카카오 ID로 DB에 해당 사용자가 이미 존재하는지 확인합니다.
     * 사용자가 존재하면 해당 사용자를 반환하여 로그인 처리를 진행하고,
     * 존재하지 않으면 새로운 사용자 계정을 생성하여 DB에 저장한 후 반환합니다.
     *
     * @param kakaoInfoDTO 카카오 사용자 정보가 담긴 DTO
     * @return 로그인 처리된 사용자 엔티티 객체
     */
    @Override
    public User kakaoLogin(KakaoInfoDTO kakaoInfoDTO) {
        // KakoInfoDTO에서 KakaoAccountDTO 객체를 가져온다.
        KakaoAccountDTO account = kakaoInfoDTO.getKakao_account();
        // 카카오 ID 기반으로 DB에 사용자 있는지 확인
        Optional<User> existingUser = userRepository.findByKakaoId(kakaoInfoDTO.getId());
        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            User user = User.builder()
                    .userName(account.getName())
                    .email(account.getEmail())
                    .password("kakao#password")
                    .phone(formatPhone(account.getPhone_number()))
                    .role(Role.USER)
                    .userState(UserState.ACTIVE)
                    .kakaoId(kakaoInfoDTO.getId())
                    .isWithdrawal(false)
                    .build();

            return userRepository.save(user);
        }
    }

    // 카카오 로그인 사용자 전화번호 포맷팅
    public String formatPhone(String phone) {
        if (phone.startsWith("+82")) {
            phone = phone.replace("+82", "");
        }
        phone = phone.replaceAll("[^0-9]", "");
        if (phone.startsWith("10")) {
            phone = "0" + phone;
        }
        return phone;
    }

    /**
     * 활동 정지 해제 메서드
     *
     * 정지된 사용자 계정을 현재 날짜 기준으로 해제합니다.
     * 정지 해제 조건을 만족하는 사용자를 조회하여, 해당 사용자들의 정지 상태를 해제합니다.
     */
    @Override
    public void releaseBans() {
        List<User> users = userRepository.findBySuspensionUntil(LocalDate.now());
        log.info(users);
        for(User user : users){
            user.releaseBans();
        }
    }

    /**

     * @param userId 주소를 확인할 유저 id
     * @return 주소가 있으면 주소, 없으면 Empty Address
     */
    @Override
    public String userAddrExist(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유효하지않은 user"));
        if(user.getAddress()!=null){
            return user.getAddress();
        }else {
            return "Empty Address";
        }

     * 사용자 주소 업데이트 메서드
     *
     * @param userAddressDTO 사용자 ID와 주소를 담은 DTO
     */
    @Override
    public void updateUserAddress(UserAddressDTO userAddressDTO) {
        User user = userRepository.findById(userAddressDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 사용자 주소 업데이트
        user.setAddress(userAddressDTO.getAddress());
        userRepository.save(user);

    }
}