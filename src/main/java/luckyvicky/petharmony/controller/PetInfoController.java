package luckyvicky.petharmony.controller;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.service.PetInfoFormatService;
import luckyvicky.petharmony.repository.PetInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
     * 모든 유기동물 정보를 반환하는 엔드포인트 (페이징 처리 및 캐싱 포함)
     *
     * @param page 페이지 번호 (기본값 0)
     * @param size 페이지 크기 (기본값 12)
     * @return 유기동물 정보 목록 (JSON 형식)
     */
    @GetMapping("/public/allPetsInfo")
    @Cacheable(value = "pets", key = "#page + '-' + #size")
    public List<Map<String, Object>> getAllPetsInfo(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "12") int size) {

        // 페이징 정보를 포함하여 PetInfo 데이터를 가져옴
        Pageable pageable = PageRequest.of(page, size);
        Page<PetInfo> petInfoPage = petInfoRepository.findAll(pageable);

        // 각 PetInfo 객체를 처리하여 프론트엔드에 전달할 형식으로 변환
        return petInfoPage.stream()
                .map(petInfoFormatService::processPetInfo)
                .collect(Collectors.toList());
    }
}
