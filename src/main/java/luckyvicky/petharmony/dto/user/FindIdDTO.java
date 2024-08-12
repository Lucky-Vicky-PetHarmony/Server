package luckyvicky.petharmony.dto.user;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindIdDTO {
    private String phone;     // 전화번호

    private String certificationNumber;  // 인증번호
}