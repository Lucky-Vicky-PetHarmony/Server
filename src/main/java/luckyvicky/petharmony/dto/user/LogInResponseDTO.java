package luckyvicky.petharmony.dto.user;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogInResponseDTO {
    private String jwtToken;

    private Long userId;

    private String email;

    private String userName;

    private String role;
}
