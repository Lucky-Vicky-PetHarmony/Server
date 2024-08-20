package luckyvicky.petharmony.security;

import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.user.KakaoLogInResponseDTO;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Map;
import java.util.Optional;

@Log4j2
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, ClientRegistrationRepository clientRegistrationRepository, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/public/**").permitAll()  // 공용 엔드포인트
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")  // 관리자 엔드포인트
                        .requestMatchers("/api/user/**").hasRole("USER")  // 사용자 엔드포인트
                        .anyRequest().authenticated()  // 그 외 모든 요청은 인증 필요
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/oauth?error=true")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService(clientRegistrationRepository)))
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // Spring Security의 OAuth2 로그인 플로우에 따라 카카오에서 인증을 완료한 후 사용자 정보 처리
    // 현재 사용하고 있지 않음 (수정 예정)
    // 현재는 UserService에서 Spring Security와는 별도로 카카오 API에 직접 요청을 보내고 응답 처리
    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(ClientRegistrationRepository clientRegistrationRepository) {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return request -> {
            OAuth2User oAuth2User = delegate.loadUser(request);
            Map<String, Object> attributes = oAuth2User.getAttributes();

            String kakaoId = String.valueOf(attributes.get("id"));
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

            String email = (String) kakaoAccount.get("email");
            String userName = (String) kakaoAccount.get("name");
            String phone = (String) kakaoAccount.get("phone_number");

            Optional<User> optionalUser = userRepository.findByKakaoId(kakaoId);
            User user = optionalUser.orElseGet(() -> {
                User newUser = User.builder()
                        .userName(userName)
                        .email(email)
                        .phone(phone)
                        .role(Role.USER)
                        .userState(UserState.ACTIVE)
                        .kakaoId(kakaoId)
                        .build();
                return userRepository.save(newUser);
            });

            String jwtToken = jwtTokenProvider.generateToken(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            "kakao#password"
                    )
            );

            KakaoLogInResponseDTO response = new KakaoLogInResponseDTO(
                    jwtToken,
                    user.getEmail(),
                    user.getUserName(),
                    user.getRole().toString()
            );

            return oAuth2User;
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}