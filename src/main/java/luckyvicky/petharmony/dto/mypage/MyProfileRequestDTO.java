package luckyvicky.petharmony.dto.mypage;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyProfileRequestDTO {
    private String userName;

    private String email;

    private String phone;
}
