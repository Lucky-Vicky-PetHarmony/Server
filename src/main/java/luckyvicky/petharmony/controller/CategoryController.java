package luckyvicky.petharmony.controller;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.repository.PetInfoRepository; // 추가: PetInfoRepository 사용
import luckyvicky.petharmony.service.PetInfoFormatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
public class CategoryController {

    private final PetInfoFormatService petInfoFormatService;
    private final PetInfoRepository petInfoRepository;  // PetInfoRepository 주입

    @Autowired
    public CategoryController(PetInfoFormatService petInfoFormatService, PetInfoRepository petInfoRepository) {
        this.petInfoFormatService = petInfoFormatService;
        this.petInfoRepository = petInfoRepository;  // PetInfoRepository 초기화
    }

    /**
     * '개'에 해당하는 PetInfo 데이터를 반환하는 엔드포인트
     *
     * @return '개'에 해당하는 포맷된 PetInfo 데이터
     */
    @GetMapping("/pets/categories/dogs")
    public List<Map<String, Object>> getDogs() {
        List<PetInfo> petInfos = petInfoRepository.findAll();  // 모든 PetInfo 데이터를 가져옴

        // 'kind_cd'가 '개'인 데이터를 필터링하고 포맷하여 반환
        return petInfos.stream()
                .filter(pet -> "개".equals(petInfoFormatService.processKindCd(pet.getKindCd())))
                .map(petInfoFormatService::processPetInfo)
                .collect(Collectors.toList());
    }

    /**
     * '고양이'에 해당하는 PetInfo 데이터를 반환하는 엔드포인트
     *
     * @return '고양이'에 해당하는 포맷된 PetInfo 데이터
     */
    @GetMapping("/pets/categories/cats")
    public List<Map<String, Object>> getCats() {
        List<PetInfo> petInfos = petInfoRepository.findAll();  // 모든 PetInfo 데이터를 가져옴

        // 'kind_cd'가 '고양이'인 데이터를 필터링하고 포맷하여 반환
        return petInfos.stream()
                .filter(pet -> "고양이".equals(petInfoFormatService.processKindCd(pet.getKindCd())))
                .map(petInfoFormatService::processPetInfo)
                .collect(Collectors.toList());
    }

    /**
     * '기타축종'에 해당하는 PetInfo 데이터를 반환하는 엔드포인트
     *
     * @return '기타축종'에 해당하는 포맷된 PetInfo 데이터
     */
    @GetMapping("/pets/categories/others")
    public List<Map<String, Object>> getOtherAnimals() {
        List<PetInfo> petInfos = petInfoRepository.findAll();  // 모든 PetInfo 데이터를 가져옴

        // 'kind_cd'가 '기타축종'인 데이터를 필터링하고 포맷하여 반환
        return petInfos.stream()
                .filter(pet -> "기타축종".equals(petInfoFormatService.processKindCd(pet.getKindCd())))
                .map(petInfoFormatService::processPetInfo)
                .collect(Collectors.toList());
    }

}
