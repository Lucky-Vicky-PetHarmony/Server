package luckyvicky.petharmony.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoLogInResponseDTO {
    private boolean success;

    private String email;

    private String userName;
}
