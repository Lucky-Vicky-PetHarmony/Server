package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.PetLike;
import luckyvicky.petharmony.entity.ShelterInfo;
import luckyvicky.petharmony.repository.PetInfoRepository;
import luckyvicky.petharmony.repository.PetLikeRepository;
import luckyvicky.petharmony.repository.ShelterInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CombinedInfoService {

    private final PetInfoRepository petInfoRepository;
    private final ShelterInfoRepository shelterInfoRepository;
    private final DetailAdoptionService detailAdoptionService;
    private final ShelterInfoService shelterInfoService;
    private final PetLikeRepository petLikeRepository;

    @Autowired
    public CombinedInfoService(PetInfoRepository petInfoRepository,
                               ShelterInfoRepository shelterInfoRepository,
                               DetailAdoptionService detailAdoptionService,
                               ShelterInfoService shelterInfoService,
                               PetLikeRepository petLikeRepository) {
        this.petInfoRepository = petInfoRepository;
        this.shelterInfoRepository = shelterInfoRepository;
        this.detailAdoptionService = detailAdoptionService;
        this.shelterInfoService = shelterInfoService;
        this.petLikeRepository = petLikeRepository;
    }

    /**
     * 유기동물 정보와 보호소 정보를 결합하여 반환하는 메서드
     *
     * @param desertionNo 유기동물 번호
     * @param userId      사용자 ID
     * @return 유기동물 정보와 보호소 정보가 결합된 맵 객체
     */
    public Map<String, Object> getCombinedInfo(String desertionNo, Long userId) {
        Map<String, Object> resultMap = new HashMap<>();

        // 첫 번째 쿼리: PetInfo 가져오기
        PetInfo petInfo = petInfoRepository.findByDesertionNo(desertionNo);
        if (petInfo != null) {
            Map<String, Object> petInfoDetails = detailAdoptionService.processPetInfo(petInfo, userId);
            resultMap.put("pet_info", petInfoDetails);

            // 두 번째 쿼리: ShelterInfo 가져오기
            Optional<ShelterInfo> shelterInfoOptional = shelterInfoRepository.findByCareNm(petInfo.getCareNm());
            if (shelterInfoOptional.isPresent()) {
                ShelterInfo shelterInfo = shelterInfoOptional.get();
                Map<String, Object> shelterInfoDetails = shelterInfoService.processShelterInfo(shelterInfo);
                resultMap.put("shelter_info", shelterInfoDetails);
            } else {
                resultMap.put("shelter_info", "보호소 정보를 찾을 수 없습니다.");
            }
        } else {
            resultMap.put("pet_info", "유기동물 정보를 찾을 수 없습니다.");
        }

        return resultMap;
    }
}
