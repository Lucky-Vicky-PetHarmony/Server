package luckyvicky.petharmony.dto.mypage;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteAccountResponseDTO {
    private String email;

    private String userName;

    private String role;
}
