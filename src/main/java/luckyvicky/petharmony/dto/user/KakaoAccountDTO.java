package luckyvicky.petharmony.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoAccountDTO {
    private String email;

    private String name;

    private String phone_number;
}
