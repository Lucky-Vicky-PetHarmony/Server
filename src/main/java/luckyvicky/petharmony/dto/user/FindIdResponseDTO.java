package luckyvicky.petharmony.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
