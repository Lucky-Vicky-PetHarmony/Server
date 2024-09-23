package luckyvicky.petharmony.dto.user;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindIdResponseDTO {
    private String email;

    private LocalDateTime createDate;

    private String responseMsg;
}
