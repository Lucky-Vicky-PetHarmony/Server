package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.user.*;
import luckyvicky.petharmony.entity.Certification;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.repository.CertificationRepository;
import luckyvicky.petharmony.repository.UserRepository;
import luckyvicky.petharmony.security.Role;
import luckyvicky.petharmony.security.UserState;
import luckyvicky.petharmony.util.EmailUtil;
import luckyvicky.petharmony.util.SmsUtil;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final SmsUtil smsUtil;
    private final CertificationRepository certificationRepository;
    private final EmailUtil emailUtil;

    // 자체 회원가입 메서드
    @Override
    @Transactional
    public User signUp(SignUpDTO signUpDTO) {
        Optional<User> withdrawanUser = userRepository.findByIsWithdrawalTrueAndEmail(signUpDTO.getEmail());
        if (withdrawanUser.isPresent()) {
            throw new IllegalArgumentException("🐶해당 이메일은 탈퇴한 계정입니다." +
                    "\npetharmony77@gmail.com로 문의주세요.");
        }

        Optional<User> kakaoUser = userRepository.findByEmailAndKakaoIdIsNotNull(signUpDTO.getEmail());
        if (kakaoUser.isPresent()) {
            throw new IllegalArgumentException("🐶카카오 로그인으로 회원가입한 사용자입니다." +
                    "\n[카카오로 시작하기]로 로그인을 진행해주세요.");
        }

        Optional<User> existingUser = userRepository.findByEmail(signUpDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("🐶이미 사용 중인 이메일입니다." +
                    "\n다른 이메일로 회원가입을 진행해주세요.");
        }

        String encodedPassword = passwordEncoder.encode(signUpDTO.getPassword());

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

    // 아이디 찾기를 위한 인증번호 전송 메서드
    @Override
    @Transactional
    public String sendingNumberToFindId(FindIdDTO findIdDTO) {
        Optional<User> optionalUser = userRepository.findByPhoneAndKakaoIdIsNull(findIdDTO.getPhone());

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

    // 아이디 찾기 시 인증번호 확인 메서드
    @Override
    public FindIdResponseDTO checkNumberToFindid(FindIdDTO findIdDTO) {
        Optional<Certification> optionalCertification = certificationRepository.findTopByPhoneOrderByCreateDateDesc(findIdDTO.getPhone());

        if (optionalCertification.isPresent() && optionalCertification.get().getCertificationNumber().equals(findIdDTO.getCertificationNumber())) {
            Optional<User> optionalUser = userRepository.findByPhoneAndKakaoIdIsNull(findIdDTO.getPhone());

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

    // 비밀번호 찾기 시 임시 비밀번호 이메일 전송 메서드
    @Override
    public String sendingEmailToFindPassword(FindPasswordDTO findPasswordDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(findPasswordDTO.getEmail());

        if (optionalUser.isPresent() && optionalUser.get().getKakaoId() != null) {
            return "카카오톡으로 로그인한 사용자입니다.";
        }

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String certificationCode = String.format("%08d", (int) (Math.random() * 100000000));

            String encodedPassword = passwordEncoder.encode(certificationCode);

            user.updatePassword(encodedPassword);

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

    // 활동 정지 해제 메서드
    @Override
    public void releaseBans() {
        List<User> users = userRepository.findBySuspensionUntil(LocalDate.now());
        log.info(users);
        for(User user : users){
            user.releaseBans();
        }
    }

    // 사용자 주소 확인 메서드
    @Override
    public String userAddrExist(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유효하지않은 user"));
        if (user.getAddress() != null) {
            return user.getAddress();
        } else {
            return "Empty Address";
        }
    }

    // 사용자 주소를 업데이트 하는 메서드
    @Override
    public void updateUserAddress(UserAddressDTO userAddressDTO) {
        User user = userRepository.findById(userAddressDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 사용자 주소 업데이트
        user.setAddress(userAddressDTO.getAddress());
        userRepository.save(user);

    }
}