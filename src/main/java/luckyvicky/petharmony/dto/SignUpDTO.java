package luckyvicky.petharmony.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {
    private String userName;     // 이름

    private String email;        // 이메일

    private String password;     // 비밀번호

    private String phone;        // 전화번호
}
