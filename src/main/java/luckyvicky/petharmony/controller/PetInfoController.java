package luckyvicky.petharmony.controller;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.service.PetInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PetInfoController {

    private final PetInfoService petInfoService;

    public PetInfoController(PetInfoService petInfoService) {
        this.petInfoService = petInfoService;
    }

    @GetMapping("/pets/top12/{userId}")
    public List<PetInfo> getTop12PetsByUserWord(@PathVariable Long userId) {
        return petInfoService.getTop12PetInfosByUserWord(userId);
    }
}
