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

    private final LocationService locationService;
    private final ShelterInfoRepository shelterInfoRepository;
    private final UserRepository userRepository;
    private final PetInfoRepository petInfoRepository;

    @Autowired
    public LocationPetService(LocationService locationService, ShelterInfoRepository shelterInfoRepository,
                              UserRepository userRepository, PetInfoRepository petInfoRepository) {
        this.locationService = locationService;
        this.shelterInfoRepository = shelterInfoRepository;
        this.userRepository = userRepository;
        this.petInfoRepository = petInfoRepository;
    }

    public Mono<List<PetInfo>> getPetInfosWithin30km(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Mono.error(new UserNotFoundException("User not found with ID: " + userId));
        }

        User user = userOpt.get();
        if (user.getAddress() == null || user.getAddress().isEmpty()) {
            return Mono.error(new AddressNotFoundException("사용자 주소가 없습니다."));
        }

        return Mono.just(locationService.getLatLonFromAddress(user.getAddress()))
                .flatMap(userCoords -> {
                    List<ShelterInfo> allShelters = shelterInfoRepository.findAll();

                    return Flux.fromIterable(allShelters)
                            .map(shelter -> {
                                double[] shelterCoords = locationService.getLatLonFromShelterInfo(shelter);
                                return new ShelterDistance(shelter, locationService.calculateDistance(userCoords[0], userCoords[1], shelterCoords[0], shelterCoords[1]));
                            })
                            .collectList()
                            .flatMap(shelterDistances -> {
                                List<ShelterInfo> sheltersWithin30km = new ArrayList<>();
                                for (ShelterDistance shelterDistance : shelterDistances) {
                                    if (shelterDistance.distance <= 30) {
                                        sheltersWithin30km.add(shelterDistance.shelterInfo);
                                    }
                                }

                                if (sheltersWithin30km.isEmpty()) {
                                    shelterDistances.sort(Comparator.comparingDouble(shelterDistance -> shelterDistance.distance));
                                    sheltersWithin30km = shelterDistances.subList(0, Math.min(shelterDistances.size(), 30))
                                            .stream()
                                            .map(shelterDistance -> shelterDistance.shelterInfo)
                                            .toList();
                                }

                                List<String> careNmList = sheltersWithin30km.stream()
                                        .map(ShelterInfo::getCareNm)
                                        .toList();

                                List<PetInfo> petInfos = petInfoRepository.findAllByCareNmIn(careNmList);
                                return Mono.just(petInfos);
                            });
                });
    }

    private static class ShelterDistance {
        ShelterInfo shelterInfo;
        double distance;

        ShelterDistance(ShelterInfo shelterInfo, double distance) {
            this.shelterInfo = shelterInfo;
            this.distance = distance;
        }
    }

    private static class AddressNotFoundException extends RuntimeException {
        public AddressNotFoundException(String message) {
            super(message);
        }
    }

    private static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
