package luckyvicky.petharmony.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindPasswordDTO {
    private String email;     // 이메일
}
