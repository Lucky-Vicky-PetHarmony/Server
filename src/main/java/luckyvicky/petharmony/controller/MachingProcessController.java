package luckyvicky.petharmony.controller;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.service.MatchingProcessService;
import luckyvicky.petharmony.service.WordMatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MachingProcessController는 사용자와 관련된 단어 매칭 및 PetInfo 데이터 처리를 담당하는 REST 컨트롤러입니다.
 * 사용자가 선택한 단어와 매칭되는 반려동물 정보를 반환합니다.
 */
@RestController
public class MachingProcessController {

    private final WordMatchingService wordMatchingService;
    private final MatchingProcessService petInfoProcessingService;

    /**
     * MachingProcessController의 생성자입니다.
     * WordMatchingService와 MatchingProcessService를 주입받습니다.
     *
     * @param wordMatchingService 사용자가 선택한 단어와 매칭되는 반려동물 정보를 찾는 서비스
     * @param petInfoProcessingService 매칭된 반려동물 정보를 처리하는 서비스
     */
    @Autowired
    public MachingProcessController(WordMatchingService wordMatchingService, MatchingProcessService petInfoProcessingService) {
        this.wordMatchingService = wordMatchingService;
        this.petInfoProcessingService = petInfoProcessingService;
    }

    /**
     * 사용자가 선택한 단어와 매칭되는 상위 12개의 반려동물 정보를 반환하는 API 엔드포인트입니다.
     *
     * @param userId 현재 로그인된 사용자의 ID
     * @return 사용자가 선택한 단어와 매칭된 반려동물 정보의 리스트, 각각의 반려동물 정보는 Map<String, Object>로 반환됩니다.
     */
    @GetMapping("/user/top12/{userId}")
    public List<Map<String, Object>> getTop12PetsByUserWord(@PathVariable Long userId) {
        // 1. 사용자의 단어와 매칭된 반려동물 정보를 가져옵니다.
        List<PetInfo> matchedPetInfos = wordMatchingService.getTop12PetInfosByUserWord(userId);

        // 2. 매칭된 반려동물 정보를 추가 처리하여 반환합니다.
        // 각 PetInfo 객체를 MatchingProcessService를 통해 처리하고, 그 결과를 리스트로 수집합니다.
        return matchedPetInfos.stream()
                .map(petInfoProcessingService::processPetInfo) // 각 PetInfo를 처리하여 결과를 Map으로 변환
                .collect(Collectors.toList()); // 결과를 리스트로 수집하여 반환
    }
}
