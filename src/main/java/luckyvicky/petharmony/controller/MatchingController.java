package luckyvicky.petharmony.controller;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.service.PetInfoFormatService;
import luckyvicky.petharmony.service.WordMatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class MatchingController {

    private final WordMatchingService wordMatchingService;
    private final PetInfoFormatService petInfoFormatService;

    @Autowired
    public MatchingController(WordMatchingService wordMatchingService, PetInfoFormatService petInfoFormatService) {
        this.wordMatchingService = wordMatchingService;
        this.petInfoFormatService = petInfoFormatService;
    }

    /**
     * 사용자의 단어와 매칭된 상위 12개의 반려동물 정보를 반환하는 엔드포인트
     *
     * @param userId 사용자의 ID
     * @return 매칭된 반려동물 정보 목록 (JSON 형식)
     */
    @GetMapping("/api/user/top12/{userId}")
    public List<Map<String, Object>> getTop12PetsByUserWord(@PathVariable Long userId) {
        // 사용자의 wordId 리스트와 매칭된 PetInfo 리스트를 가져옴
        List<PetInfo> matchedPetInfos = wordMatchingService.getTop12PetInfosByUserWord(userId);

        // 각 PetInfo 객체를 처리하여 프론트엔드에 전달할 형식으로 변환
        return matchedPetInfos.stream()
                .map(petInfoFormatService::processPetInfo)
                .collect(Collectors.toList());
    }
}
