package luckyvicky.petharmony.controller;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.service.PetInfoFormatService;
import luckyvicky.petharmony.repository.PetInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PetInfoController {

    private final PetInfoFormatService petInfoFormatService;
    private final PetInfoRepository petInfoRepository;

    @Autowired
    public PetInfoController(PetInfoFormatService petInfoFormatService, PetInfoRepository petInfoRepository) {
        this.petInfoFormatService = petInfoFormatService;
        this.petInfoRepository = petInfoRepository;
    }

    /**
     * 모든 유기동물 정보를 반환하는 엔드포인트
     *
     * @return 유기동물 정보 목록 (JSON 형식)
     */
    @GetMapping("user/allPetsInfo")
    public List<Map<String, Object>> getAllPetsInfo() {
        // 모든 PetInfo 데이터를 가져옴
        List<PetInfo> allPetInfos = petInfoRepository.findAll();

        // 각 PetInfo 객체를 처리하여 프론트엔드에 전달할 형식으로 변환
        return allPetInfos.stream()
                .map(petInfoFormatService::processPetInfo)
                .collect(Collectors.toList());
    }
}
