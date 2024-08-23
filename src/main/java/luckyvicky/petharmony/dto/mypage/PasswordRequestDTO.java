package luckyvicky.petharmony.dto.mypage;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRequestDTO {
    private String prePassword;

    private String newPassword;
}