/*
package luckyvicky.petharmony.controller;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.service.AllMatchingService;
import luckyvicky.petharmony.service.PetInfoFormatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class MatchingController {

    private final AllMatchingService allMatchingService;
    private final PetInfoFormatService petInfoFormatService;

    @Autowired
    public MatchingController(AllMatchingService allMatchingService, PetInfoFormatService petInfoFormatService) {
        this.allMatchingService = allMatchingService;
        this.petInfoFormatService = petInfoFormatService;
    }

    */
/**
     * 사용자 ID로 매칭된 PetInfo 데이터를 가져와 포맷된 형태로 반환하는 메서드
     *
     * @param userId 사용자 ID
     * @return 포맷된 PetInfo 데이터 리스트
     *//*

    @GetMapping("/user/matching/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getMatchingPetInfos(@PathVariable Long userId) {
        return allMatchingService.getTop12PetInfos(userId)
                .map(petInfos -> petInfos.stream()
                        .map(petInfo -> petInfoFormatService.processPetInfo(petInfo, userId))
                        .collect(Collectors.toList()))
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    // 오류 처리: 예외 메시지와 함께 빈 리스트 반환
                    return Mono.just(ResponseEntity.badRequest().body(List.of(Map.of("error", e.getMessage()))));
                })
                .block(); // 비동기 결과를 동기적으로 반환
    }
}
*/
