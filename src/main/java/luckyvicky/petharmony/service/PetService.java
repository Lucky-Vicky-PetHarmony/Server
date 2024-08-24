package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.ShelterInfo;
import luckyvicky.petharmony.repository.PetInfoRepository;
import luckyvicky.petharmony.repository.ShelterInfoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PetService {

    private final LocationService locationService; // 위치 정보를 가져오는 LocationService
    private final ShelterInfoRepository shelterInfoRepository; // 보호소 정보와 관련된 데이터베이스 작업을 처리하는 Repository
    private final PetInfoRepository petInfoRepository; // 반려동물 정보와 관련된 데이터베이스 작업을 처리하는 Repository

    public PetService(LocationService locationService, ShelterInfoRepository shelterInfoRepository, PetInfoRepository petInfoRepository) {
        this.locationService = locationService;
        this.shelterInfoRepository = shelterInfoRepository;
        this.petInfoRepository = petInfoRepository;
    }

    /**
     * 사용자의 주소를 기반으로 가까운 보호소에서 발견된 모든 반려동물 정보를 반환
     *
     * @param address 사용자의 주소 (우편번호 포함)
     * @return 주어진 주소에서 30km 이내에 위치한 보호소의 모든 반려동물 정보 리스트
     */
    public List<PetInfo> getPetsNearUser(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("주소가 유효하지 않습니다.");
        }

        // 사용자의 주소로부터 위도와 경도를 계산해서 가져옴
        double[] userLatLng = locationService.getLatLonFromAddress(address);
        double userLat = userLatLng[0];
        double userLon = userLatLng[1];

        // 사용자의 위치로부터 30km 이내에 있는 모든 보호소 정보를 검색
        List<ShelterInfo> nearbyShelters = shelterInfoRepository.findWithinDistance(userLat, userLon, 30.0);
        List<PetInfo> pets = new ArrayList<>(); // 반환할 반려동물 정보 리스트

        if (!nearbyShelters.isEmpty()) {
            // 30km 이내에 보호소가 있을 경우 해당 보호소의 모든 반려동물 정보를 가져옴
            for (ShelterInfo shelter : nearbyShelters) {
                pets.addAll(petInfoRepository.findAllByCareNm(shelter.getCareNm()));
            }
        } else {
            // 30km 이내에 보호소가 없을 경우, 사용자의 위치에서 가장 가까운 30개의 보호소 정보를 가져옴
            Pageable pageable = PageRequest.of(0, 30); // 가장 가까운 30개의 보호소만 가져오도록 Pageable 객체를 생성
            List<ShelterInfo> nearestShelters = shelterInfoRepository.findNearestShelters(userLat, userLon, pageable);
            for (ShelterInfo shelter : nearestShelters) {
                pets.addAll(petInfoRepository.findAllByCareNm(shelter.getCareNm()));
            }
        }

        return pets;
    }
}
