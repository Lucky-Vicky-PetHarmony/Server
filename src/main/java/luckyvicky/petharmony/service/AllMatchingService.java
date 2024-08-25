/*
package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AllMatchingService {

    private final WordMatchingService wordMatchingService;
    private final LocationPetService locationPetService;
    private final UserRepository userRepository;

    @Autowired
    public AllMatchingService(WordMatchingService wordMatchingService, LocationPetService locationPetService, UserRepository userRepository) {
        this.wordMatchingService = wordMatchingService;
        this.locationPetService = locationPetService;
        this.userRepository = userRepository;
    }

    public List<PetInfo> getCombinedPetInfos(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            System.out.println("User ID: " + userId + "에 해당하는 사용자가 없습니다.");
            return List.of();
        }
        String address = userOptional.get().getAddress();

        if (address == null || address.trim().isEmpty()) {
            System.out.println("User ID: " + userId + "의 주소 정보가 없습니다.");
            return List.of();
        }

        // 디버깅 로그 추가
        System.out.println("User ID: " + userId + "의 주소: " + address);

        List<PetInfo> matchedPetsByWord = wordMatchingService.getMatchingPetInfosByUserWord(userId);

        // 디버깅 로그 추가
        System.out.println("User ID: " + userId + "의 매칭된 단어 기반 반려동물 수: " + matchedPetsByWord.size());

        List<PetInfo> petsNearUser = locationPetService.getPetsNearUser(address);

        // 디버깅 로그 추가
        System.out.println("User ID: " + userId + "의 주소 근처 반려동물 수: " + petsNearUser.size());

        Set<String> matchedDesertionNos = matchedPetsByWord.stream()
                .map(PetInfo::getDesertionNo)
                .collect(Collectors.toSet());

        return petsNearUser.stream()
                .filter(pet -> matchedDesertionNos.contains(pet.getDesertionNo()))
                .sorted((pet1, pet2) -> {
                    int matchCount1 = wordMatchingService.countMatchingWords(pet1.getWords(), wordMatchingService.getWordIdListAsString(userId));
                    int matchCount2 = wordMatchingService.countMatchingWords(pet2.getWords(), wordMatchingService.getWordIdListAsString(userId));
                    return Integer.compare(matchCount2, matchCount1);
                })
                .limit(12)
                .collect(Collectors.toList());
    }
}
*/
