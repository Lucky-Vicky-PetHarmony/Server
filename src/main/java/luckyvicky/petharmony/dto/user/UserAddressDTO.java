package luckyvicky.petharmony.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressDTO {
    private Long userId;     // 사용자 ID
    private String address;  // 사용자 주소
}
