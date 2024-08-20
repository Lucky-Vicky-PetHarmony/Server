package luckyvicky.petharmony.dto.user;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoLogInResponseDTO {
    private String jwtToken;

    private String email;

    private String userName;

    private String role;
}
