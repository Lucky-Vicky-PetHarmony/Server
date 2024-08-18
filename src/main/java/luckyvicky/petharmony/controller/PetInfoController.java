package luckyvicky.petharmony.controller;


import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.service.PetInfoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pet-info")
public class PetInfoController {

    private final PetInfoService petInfoService;

    /**
     * PetInfoController 생성자
     *
     * @Param petInfoService PetInfo 서비스 레이어
     */
    public PetInfoController(PetInfoService petInfoService) {
        this.petInfoService = petInfoService;
    }

    /**
     * 사용자가 선호하는 단어에 따라 상위 12개의 PetInfo를 반환하는 API엔드포인트
     *
     * @return 사용자가 선호하는 단어와 매칭된 상위 12개의 PetInfo 리스트
     * @Parm user 현재 로그인된 사용자(시큐리티를 통해 자동 주입)
     */
    @GetMapping("/top12")
    public List<PetInfo> getTop12PetInfos(@AuthenticationPrincipal User user) {
        return petInfoService.getTop12PetInfosByUserWord(user);
    }
}
