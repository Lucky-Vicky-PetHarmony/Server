package luckyvicky.petharmony.controller;

import luckyvicky.petharmony.service.CombinedInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AnimalDetailsController {

    @Autowired
    private CombinedInfoService combinedInfoService;

    /**
     * 유기동물 정보와 보호소 정보를 결합하여 반환하는 API 엔드포인트
     */
    @GetMapping("/public/{desertionNo}/{userId}")
    public Map<String, Object> getCombinedAnimalInfo(@PathVariable String desertionNo, @PathVariable Long userId) {
        // CombinedInfoService에서 최적화된 데이터를 가져옴
        return combinedInfoService.getCombinedInfo(desertionNo, userId);
    }
}
