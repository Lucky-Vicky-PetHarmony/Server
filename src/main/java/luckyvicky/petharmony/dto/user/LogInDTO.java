package luckyvicky.petharmony.dto.user;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogInDTO {
    private String email;        // 이메일

    private String password;     // 비밀번호
}
