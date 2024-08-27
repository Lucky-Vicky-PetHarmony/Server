package luckyvicky.petharmony.controller;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.repository.PetInfoRepository; // 추가: PetInfoRepository 사용
import luckyvicky.petharmony.service.PetInfoFormatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cache.annotation.Cacheable;

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
     * '개'에 해당하는 PetInfo 데이터를 반환하는 엔드포인트 (페이징 처리 및 캐싱 포함)
     *
     * @param page 페이지 번호 (기본값 0)
     * @param size 페이지 크기 (기본값 12)
     * @return '개'에 해당하는 포맷된 PetInfo 데이터
     */
    @GetMapping("/pets/categories/dogs")
    @Cacheable(value = "dogs", key = "#page + '-' + #size")
    public List<Map<String, Object>> getDogs(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "12") int size ) {

        Pageable pageable = PageRequest.of(page, size);
        Page<PetInfo> petInfoPage = petInfoRepository.findByKindCdContaining("개", pageable);

        // 'kind_cd'가 '개'인 데이터를 필터링하고 포맷하여 반환
        return petInfoPage.stream()
                .map(petInfoFormatService::processPetInfo)
                .collect(Collectors.toList());
    }

    /**
     * '고양이'에 해당하는 PetInfo 데이터를 반환하는 엔드포인트 (페이징 처리 및 캐싱 포함)
     *
     * @param page 페이지 번호 (기본값 0)
     * @param size 페이지 크기 (기본값 12)
     * @return '고양이'에 해당하는 포맷된 PetInfo 데이터
     */
    @GetMapping("/pets/categories/cats")
    @Cacheable(value = "cats", key = "#page + '-' + #size")
    public List<Map<String, Object>> getCats(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "12") int size ) {

        Pageable pageable = PageRequest.of(page, size);
        Page<PetInfo> petInfoPage = petInfoRepository.findByKindCdContaining("고양이", pageable);

        // 'kind_cd'가 '고양이'인 데이터를 필터링하고 포맷하여 반환
        return petInfoPage.stream()
                .map(petInfoFormatService::processPetInfo)
                .collect(Collectors.toList());
    }

    /**
     * '기타축종'에 해당하는 PetInfo 데이터를 반환하는 엔드포인트 (페이징 처리 및 캐싱 포함)
     *
     * @param page 페이지 번호 (기본값 0)
     * @param size 페이지 크기 (기본값 12)
     * @return '기타축종'에 해당하는 포맷된 PetInfo 데이터
     */
    @GetMapping("/pets/categories/others")
    @Cacheable(value = "others", key = "#page + '-' + #size")
    public List<Map<String, Object>> getOtherAnimals(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "12") int size ) {

        Pageable pageable = PageRequest.of(page, size);
        Page<PetInfo> petInfoPage = petInfoRepository.findByKindCdContaining("기타축종", pageable);

        // 'kind_cd'가 '기타축종'인 데이터를 필터링하고 포맷하여 반환
        return petInfoPage.stream()
                .map(petInfoFormatService::processPetInfo)
                .collect(Collectors.toList());
    }

}
