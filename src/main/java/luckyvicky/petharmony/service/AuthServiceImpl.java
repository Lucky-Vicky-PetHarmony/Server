package luckyvicky.petharmony.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import luckyvicky.petharmony.dto.user.KakaoAccountDTO;
import luckyvicky.petharmony.dto.user.KakaoInfoDTO;
import luckyvicky.petharmony.dto.user.LogInDTO;
import luckyvicky.petharmony.dto.user.LogInResponseDTO;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.repository.UserRepository;
import luckyvicky.petharmony.security.CustomUserDetails;
import luckyvicky.petharmony.security.JwtTokenProvider;
import luckyvicky.petharmony.security.Role;
import luckyvicky.petharmony.security.UserState;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    /**
     * 자체 로그인 메서드
     */
    @Override
    public LogInResponseDTO login(LogInDTO logInDTO) {
        try {
            Optional<User> kakaoUser = userRepository.findByEmailAndKakaoIdIsNotNull(logInDTO.getEmail());

            if (kakaoUser.isPresent()) {
                throw new IllegalStateException("🐶카카오로 회원가입한 사용자입니다." +
                        "\n카카오 로그인으로 진행해주세요.");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            logInDTO.getEmail(),
                            logInDTO.getPassword()
                    )
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            if (userDetails.getIsWithdrawal()) {
                throw new IllegalStateException("🐶탈퇴한 계정입니다." +
                        "\npetharmony77@gmail.com로 문의주세요.");
            }

            if (userDetails.getUserState() == UserState.BANNED) {
                throw new IllegalStateException("🐶활동이 정지된 계정입니다." +
                        "\npetharmony77@gmail.com으로 문의주세요.");
            }

            String jwtToken = jwtTokenProvider.generateAccessToken(authentication);

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
     * 카카오 로그인 메서드
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
     * 카카오 사용자 정보 조회 메서드
     */
    @Override
    public LogInResponseDTO kakaoLogin(String accessToken) {
        KakaoInfoDTO kakaoInfoDTO = getUserInfoFromKakao(accessToken);

        Optional<User> existingUser = userRepository.findByKakaoId(kakaoInfoDTO.getId());

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            KakaoAccountDTO account = kakaoInfoDTO.getKakao_account();
            user = User.builder()
                    .userName(account.getName())
                    .email(account.getEmail())
                    .password("kakao#password")
                    .phone(formatPhone(account.getPhone_number()))
                    .role(Role.USER)
                    .userState(UserState.ACTIVE)
                    .kakaoId(kakaoInfoDTO.getId())
                    .isWithdrawal(false)
                    .build();
            userRepository.save(user);
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), "kakao#password");

        String jwtToken = jwtTokenProvider.generateAccessToken(authentication);

        return LogInResponseDTO.builder()
                .jwtToken(jwtToken)
                .userId(user.getUserId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .role("[ROLE_"+user.getRole().toString()+"]")
                .build();
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
}
