/*
package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.repository.PetInfoRepository;
import luckyvicky.petharmony.repository.UserWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WordMatchingService {

    private final PetInfoRepository petInfoRepository;
    private final UserWordRepository userWordRepository;

    @Autowired
    public WordMatchingService(PetInfoRepository petInfoRepository, UserWordRepository userWordRepository) {
        this.petInfoRepository = petInfoRepository;
        this.userWordRepository = userWordRepository;
    }

    */
/**
     * 특정 사용자가 선택한 word_id와 매칭되는 PetInfo를 반환하는 메서드
     *
     * @param userId 현재 로그인된 사용자의 ID
     * @return PetInfo 리스트
     *//*

    public List<PetInfo> getMatchingPetInfosByUserWord(Long userId) {
        // userId에 해당하는 모든 word_id 리스트를 가져옴
        List<Long> wordIds = userWordRepository.findWordIdsByUserId(userId);

        if (wordIds == null || wordIds.isEmpty()) {
            System.out.println("Word ID 리스트가 비어 있습니다.");
            return List.of(); // wordIds가 비어있으면 빈 리스트 반환
        }

        // 각 wordId를 사용하여 데이터베이스에서 매칭되는 PetInfo를 가져옴
        Set<PetInfo> matchedPetInfos = wordIds.stream()
                .flatMap(wordId -> petInfoRepository.findByWordId(wordId.toString()).stream())
                .collect(Collectors.toSet());

        // 매칭된 모든 PetInfo를 리스트로 변환 후 반환
        return List.copyOf(matchedPetInfos);
    }

    */
/**
     * 주어진 words 문자열과 wordIdListAsString을 비교하여 매칭되는 단어의 개수를 세는 메서드
     *
     * @param words              콤마로 구분된 words 문자열 (pet_info의 words 필드)
     * @param wordIdListAsString 사용자가 선택한 word_id 리스트 (콤마로 구분된 문자열)
     * @return 매칭되는 단어의 수
     *//*


    public int countMatchingWords(String words, String wordIdListAsString) {
        if (words == null || words.isEmpty() || wordIdListAsString.isEmpty()) {
            return 0;
        }

        Set<String> wordSet = Set.of(words.split(","));
        Set<String> userWordSet = Set.of(wordIdListAsString.split(","));

        // 교집합의 크기를 반환
        userWordSet.retainAll(wordSet);
        return userWordSet.size();
    }

    */
/**
     * userId에 해당하는 word_id 리스트를 콤마로 구분된 문자열로 반환
     *//*

    public String getWordIdListAsString(Long userId) {
        List<Long> wordIds = userWordRepository.findWordIdsByUserId(userId);
        if (wordIds == null || wordIds.isEmpty()) {
            return "";
        }
        return wordIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
*/
