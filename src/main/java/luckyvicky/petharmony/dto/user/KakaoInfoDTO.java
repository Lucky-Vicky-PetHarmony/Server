package luckyvicky.petharmony.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoDTO {
    private Long id;

    private KakaoAccountDTO kakao_account;
}
