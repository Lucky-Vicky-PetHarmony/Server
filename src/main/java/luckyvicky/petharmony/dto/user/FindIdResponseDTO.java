package luckyvicky.petharmony.dto.user;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindIdResponseDTO {
    private String email;                 // 이메일

    private LocalDateTime createDate;     // 가입일자

    private String responseMsg;           // 응답 메시지
}
