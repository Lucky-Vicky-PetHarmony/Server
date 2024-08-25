package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.ShelterInfo;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.repository.PetInfoRepository;
import luckyvicky.petharmony.repository.ShelterInfoRepository;
import luckyvicky.petharmony.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class LocationPetService {

    // 위치 서비스, 쉘터 정보 리포지토리, 사용자 리포지토리, 펫 정보 리포지토리를 의존성 주입 받음
    private final LocationService locationService;
    private final ShelterInfoRepository shelterInfoRepository;
    private final UserRepository userRepository;
    private final PetInfoRepository petInfoRepository;

    // 생성자를 통해 의존성 주입을 받음
    @Autowired
    public LocationPetService(LocationService locationService, ShelterInfoRepository shelterInfoRepository,
                              UserRepository userRepository, PetInfoRepository petInfoRepository) {
        this.locationService = locationService;
        this.shelterInfoRepository = shelterInfoRepository;
        this.userRepository = userRepository;
        this.petInfoRepository = petInfoRepository;
    }

    // 사용자 ID를 기반으로 30km 이내의 펫 정보 목록을 가져오는 메서드
    public Mono<List<PetInfo>> getPetInfosWithin30km(Long userId) {
        // 사용자 ID로 사용자 정보를 찾음
        Optional<User> userOpt = userRepository.findById(userId);
        // 사용자가 존재하지 않으면 오류 반환
        if (userOpt.isEmpty()) {
            return Mono.error(new UserNotFoundException("User not found with ID: " + userId));
        }

        User user = userOpt.get();
        // 사용자 주소가 없으면 오류 반환
        if (user.getAddress() == null || user.getAddress().isEmpty()) {
            return Mono.error(new AddressNotFoundException("사용자 주소가 없습니다."));
        }

        // 사용자의 주소로부터 위도와 경도를 가져옴
        return Mono.just(locationService.getLatLonFromAddress(user.getAddress()))
                .flatMap(userCoords -> {
                    // 모든 쉘터 정보를 가져옴
                    List<ShelterInfo> allShelters = shelterInfoRepository.findAll();

                    // 모든 쉘터를 순회하면서 각 쉘터와 사용자 간의 거리를 계산
                    return Flux.fromIterable(allShelters)
                            .map(shelter -> {
                                double[] shelterCoords = locationService.getLatLonFromShelterInfo(shelter);
                                return new ShelterDistance(shelter, locationService.calculateDistance(userCoords[0], userCoords[1], shelterCoords[0], shelterCoords[1]));
                            })
                            .collectList()
                            .flatMap(shelterDistances -> {
                                // 30km 이내에 있는 쉘터를 필터링
                                List<ShelterInfo> sheltersWithin30km = new ArrayList<>();
                                for (ShelterDistance shelterDistance : shelterDistances) {
                                    if (shelterDistance.distance <= 30) {
                                        sheltersWithin30km.add(shelterDistance.shelterInfo);
                                    }
                                }

                                // 30km 이내에 쉘터가 없으면 가장 가까운 30개의 쉘터를 선택
                                if (sheltersWithin30km.isEmpty()) {
                                    shelterDistances.sort(Comparator.comparingDouble(shelterDistance -> shelterDistance.distance));
                                    sheltersWithin30km = shelterDistances.subList(0, Math.min(shelterDistances.size(), 30))
                                            .stream()
                                            .map(shelterDistance -> shelterDistance.shelterInfo)
                                            .toList();
                                }

                                // 필터링된 쉘터의 이름 목록을 생성
                                List<String> careNmList = sheltersWithin30km.stream()
                                        .map(ShelterInfo::getCareNm)
                                        .toList();

                                // 쉘터 이름 목록을 사용하여 해당 쉘터에 있는 펫 정보를 찾음
                                List<PetInfo> petInfos = petInfoRepository.findAllByCareNmIn(careNmList);
                                return Mono.just(petInfos);
                            });
                });
    }

    // 쉘터와 사용자 간의 거리를 나타내는 내부 클래스
    private static class ShelterDistance {
        ShelterInfo shelterInfo;
        double distance;

        // 생성자
        ShelterDistance(ShelterInfo shelterInfo, double distance) {
            this.shelterInfo = shelterInfo;
            this.distance = distance;
        }
    }

    // 사용자 주소가 없을 때 던지는 예외 클래스
    private static class AddressNotFoundException extends RuntimeException {
        public AddressNotFoundException(String message) {
            super(message);
        }
    }

    // 사용자를 찾을 수 없을 때 던지는 예외 클래스
    private static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
