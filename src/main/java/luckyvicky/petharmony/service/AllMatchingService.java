/*
package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AllMatchingService {

    // 위치 기반 매칭 서비스와 단어 기반 매칭 서비스, 사용자 리포지토리를 의존성 주입 받음
    private final LocationPetService locationPetService;
    private final WordMatchingService wordMatchingService;
    private final UserRepository userRepository;

    // 생성자를 통해 의존성 주입을 받음
    @Autowired
    public AllMatchingService(LocationPetService locationPetService, WordMatchingService wordMatchingService, UserRepository userRepository) {
        this.locationPetService = locationPetService;
        this.wordMatchingService = wordMatchingService;
        this.userRepository = userRepository;
    }

    // 사용자 ID를 기반으로 상위 12개의 PetInfo를 가져오는 메서드
    public Mono<List<PetInfo>> getTop12PetInfos(Long userId) {
        // 사용자 ID로 사용자 정보를 찾음
        Optional<User> userOpt = userRepository.findById(userId);
        // 사용자가 존재하지 않으면 오류 반환
        if (userOpt.isEmpty()) {
            return Mono.error(new UserNotFoundException("User not found with ID: " + userId));
        }

        User user = userOpt.get();

        // 사용자의 주소가 없는 경우 단어 매칭으로만 상위 12개의 PetInfo 반환
        if (user.getAddress() == null || user.getAddress().isEmpty()) {
            return getTop12ByWordMatching(userId);
        }

        // 사용자의 주소가 있는 경우 위치 기반 매칭과 단어 기반 매칭의 교집합 반환
        return locationPetService.getPetInfosWithin30km(userId)
                .flatMap(petInfosFromLocation -> {
                    // 단어 매칭을 통해 얻은 PetInfo 목록
                    List<PetInfo> petInfosFromWordMatching = wordMatchingService.getMatchingPetInfosByUserWord(userId);

                    System.out.println("Pet Infos from Location: " + petInfosFromLocation);
                    System.out.println("Pet Infos from Word Matching: " + petInfosFromWordMatching);

                    // 위치 기반 매칭과 단어 기반 매칭 결과의 교집합을 구함
                    Set<PetInfo> combinedSet = petInfosFromLocation.stream()
                            .filter(petInfosFromWordMatching::contains)
                            .collect(Collectors.toSet());

                    // 교집합을 리스트로 변환
                    List<PetInfo> combinedList = combinedSet.stream()
                            .collect(Collectors.toList());

                    // 교집합이 12개 미만일 경우 매칭률이 높은 순으로 추가
                    if (combinedList.size() < 12) {
                        // 매칭률을 기준으로 정렬된 단어 매칭 결과에서 추가
                        petInfosFromWordMatching.stream()
                                .filter(petInfo -> !combinedSet.contains(petInfo))
                                .sorted((p1, p2) -> {
//                                    int matchCount1 = wordMatchingService.countMatchingWords(p1.getWords(), wordMatchingService.getWordIdListAsString(userId));
//                                    int matchCount2 = wordMatchingService.countMatchingWords(p2.getWords(), wordMatchingService.getWordIdListAsString(userId));
                                    return 0;
                                })
                                .limit(12 - combinedList.size())
                                .forEach(combinedList::add);
                    }

                    // 최종적으로 상위 12개의 PetInfo만 반환
                    List<PetInfo> resultList = combinedList.stream()
                            .limit(12)
                            .collect(Collectors.toList());

                    System.out.println("Combined Pet Infos: " + resultList);

                    return Mono.just(resultList);
                });
    }

    // 단어 매칭을 통해 상위 12개의 PetInfo를 가져오는 메서드
    private Mono<List<PetInfo>> getTop12ByWordMatching(Long userId) {
        List<PetInfo> matchedPetInfos = wordMatchingService.getMatchingPetInfosByUserWord(userId);
        // 상위 12개의 PetInfo를 반환
        List<PetInfo> top12 = matchedPetInfos.stream().limit(12).collect(Collectors.toList());

        System.out.println("Top 12 Pet Infos from Word Matching: " + top12);

        return Mono.just(top12);
    }

    // 사용자를 찾을 수 없을 때 던지는 예외 클래스
    private static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
*/
