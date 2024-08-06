package luckyvicky.petharmony.dto;

import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindIdDTO {
    private String email;     // 이메일

    private String phone;     // 전화번호

    private LocalDateTime createDate;     // 가입일자
}
