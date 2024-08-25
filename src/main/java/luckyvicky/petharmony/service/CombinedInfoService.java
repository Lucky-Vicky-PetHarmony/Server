package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.ShelterInfo;
import luckyvicky.petharmony.repository.PetInfoRepository;
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

    @Autowired
    public CombinedInfoService(PetInfoRepository petInfoRepository,
                               ShelterInfoRepository shelterInfoRepository,
                               DetailAdoptionService detailAdoption,
                               ShelterInfoService shelterInfoService) {
        this.petInfoRepository = petInfoRepository;
        this.shelterInfoRepository = shelterInfoRepository;
        this.detailAdoptionService = detailAdoption;
        this.shelterInfoService = shelterInfoService;
    }

    /**
     * 유기동물 정보와 보호소 정보를 결합하여 반환하는 메서드
     *
     * @param desertionNo 유기동물 번호
     * @return 유기동물 정보와 보호소 정보가 결합된 맵 객체
     */
    public Map<String, Object> getCombinedInfo(String desertionNo) {
        Map<String, Object> combinedInfo = new HashMap<>();

        PetInfo petInfo = petInfoRepository.findPetInfoWithShelterByDesertionNo(desertionNo);
        if (petInfo != null) {
            Map<String, Object> petInfoDetails = detailAdoptionService.processPetInfo(petInfo);
            combinedInfo.putAll(petInfoDetails);

            Optional<ShelterInfo> shelterInfoOptional = shelterInfoRepository.findByCareNm(petInfo.getCareNm());
            if (shelterInfoOptional.isPresent()) {
                Map<String, Object> shelterInfoDetails = shelterInfoService.processShelterInfo(shelterInfoOptional.get());
                combinedInfo.putAll(shelterInfoDetails);
            } else {
                combinedInfo.put("shelter_info", "보호소 정보를 찾을 수 없습니다.");
            }
        } else {
            combinedInfo.put("pet_info", "유기동물 정보를 찾을 수 없습니다.");
        }

        return combinedInfo;
    }
}
