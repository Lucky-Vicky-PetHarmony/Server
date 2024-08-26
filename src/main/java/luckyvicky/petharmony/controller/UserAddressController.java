package luckyvicky.petharmony.controller;

import luckyvicky.petharmony.dto.user.UserAddressDTO;
import luckyvicky.petharmony.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserAddressController {

    private final UserService userService;

    @Autowired
    public UserAddressController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 사용자 주소 업데이트 엔드포인트
     *
     * @param userAddressDTO 사용자 ID와 주소를 담은 DTO
     * @return 업데이트 결과 메시지
     */
    @PostMapping("/address")
    public ResponseEntity<String> updateAddress(@RequestBody UserAddressDTO userAddressDTO) {
        userService.updateUserAddress(userAddressDTO);
        return ResponseEntity.ok("주소가 성공적으로 업데이트되었습니다.");
    }
}
