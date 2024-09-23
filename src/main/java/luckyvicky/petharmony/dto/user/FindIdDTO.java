package luckyvicky.petharmony.dto.user;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindIdDTO {
    private String phone;

    private String certificationNumber;
}