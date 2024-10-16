package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.ShelterInfo;
import luckyvicky.petharmony.entity.Word;
import luckyvicky.petharmony.repository.PetLikeRepository;
import luckyvicky.petharmony.repository.ShelterInfoRepository;
import luckyvicky.petharmony.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * PetInfoFormatService는 매칭된 PetInfo 객체를 처리하여 원하는 형식으로 반환하는 서비스입니다.
 */
@Service
public class PetInfoFormatService implements PetInfoFormatter {

    private final WordRepository wordRepository;
    private final ShelterInfoRepository shelterInfoRepository;
    private final PetLikeRepository petLikeRepository;

    @Autowired
    public PetInfoFormatService(WordRepository wordRepository, ShelterInfoRepository shelterInfoRepository, PetLikeRepository petLikeRepository) {
        this.wordRepository = wordRepository;
        this.shelterInfoRepository = shelterInfoRepository;
        this.petLikeRepository = petLikeRepository;
    }

    /**
     * 주어진 PetInfo 객체를 처리하여 원하는 형식의 데이터를 반환합니다.
     *
     * @param petInfo 처리할 PetInfo 객체
     * @return 처리된 데이터를 담고 있는 Map 객체
     */
    public Map<String, Object> processPetInfo(PetInfo petInfo, Long userId) {
        Map<String, Object> result = new HashMap<>();
        // 입양동물 번호
        result.put("desertion_no", petInfo.getDesertionNo());

        // words 필드에서 wordId를 추출하여 Word엔티티의 wordSelect 값을 매핑
        result.put("words", processWords(petInfo.getWords()));

        // kind_cd 필드를 처리하여 반환 ex)개, 고양이
        result.put("kind_cd", processKindCd(petInfo.getKindCd()));

        // kind_cd_detail 필드를 처리하여 반환 ex)말티즈, 믹스견
        result.put("kind_cd_detail", processKindCdDetail(petInfo.getKindCd()));

        // age 필드를 처리하여 반환
        result.put("age", processAge(petInfo.getAge()));

        // sex_cd 필드를 처리하여 반환
        result.put("sex_cd", processSexCd(petInfo.getSexCd()));

        // neuter_yn 필드를 처리하여 반환
        result.put("neuter_yn", processNeuterYn(petInfo.getNeuterYn()));

        // care_nm필드를 처리하여 반환
        result.put("org_nm", processLocation(petInfo.getCareNm()));

        // popfile필드를 결과에 추가하여 유기동물의 이미지 경로를 반환
        result.put("popfile", petInfo.getPopfile());

        // weight필드를 결과에 추가하여 유기동물의 무게를 반환
        result.put("weight", petInfo.getWeight());

        result.put("pet_like",
                petLikeRepository.findByUser_UserIdAndDesertionNo(userId, petInfo.getDesertionNo()).isPresent());

        return result; // 최종 처리된 데이터를 Map으로 반환
    }

    /**
     * words 필드를 처리하여 반환하는 메서드
     * words필드와 매칭된 word_id의 word_select값을 반환. 최대 3개의 단어만 선택하여 결과에 추가
     *
     * @param words pet_info 테이블의 words 필드 값
     * @return 매칭된 단어들의 리스트*/
    protected List<String> processWords(String words) {
        if (words == null || words.isEmpty()) {
            // words 필드가 비었을 때
            return Collections.emptyList();
        }
        // words를 콤마로 구분된 wordId 리스트로 반환
        List<Long> wordIds = Arrays.stream(words.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        // wordId 리스트로 Word엔티티를 조회하고, wordSelect 값을 추출하여 반환
        List<Word> wordEntities = wordRepository.findByWordIdIn(wordIds);
        List<String> wordSelects = wordEntities.stream()
                .map(Word::getWordSelect)
                .collect(Collectors.toList());

        // 단어의 개수가 3개 이하일 경우 그대로 반환
        if (wordSelects.size() <= 3) {
            return wordSelects;
        }

        // 단어가 4개 이상인 경우 3개의 랜덤한 단어를 선택하여 반환
        Collections.shuffle(wordSelects);
        return wordSelects.subList(0, 3);
    }

    /**
     * kind_cd 필드를 처리하여 반환하는 메서드
     * '기타축종'이라는 단어가 포함된 경우 대괄호([]) 바깥의 값을 반환하고,
     * 포함되지 않은 경우 대괄호 안의 값을 반환합니다.
     *
     * @param kindCd 처리할 kind_cd 필드 값
     * @return 처리된 kind_cd 문자열
     */
    private String processKindCdDetail(String kindCd) {
        // "[개] 포메라니안" 형식에서 "포메라니안"만 추출
        if (kindCd.contains("[") && kindCd.contains("]")) {
            return kindCd.substring(kindCd.indexOf("]") + 1).trim();
        } else {
            return kindCd;
        }
    }

    /**
     * kind_cd 필드를 처리하여 반환하는 메서드
     * '기타축종'이라는 단어가 포함된 경우 대괄호([]) 바깥의 값을 반환하고,
     * 포함되지 않은 경우 대괄호 안의 값을 반환합니다.
     *
     * @param kindCd 처리할 kind_cd 필드 값
     * @return 처리된 kind_cd 문자열
     */
    public String processKindCd(String kindCd) {
        if (kindCd.contains("기타축종")) {
            return "기타축종";
        }
        int startIndex = kindCd.indexOf("[");
        int endIndex = kindCd.indexOf("]");

        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return kindCd.substring(startIndex + 1, endIndex).trim();
        } else {
            return kindCd.trim();
        }
    }


    /**
     * age 필드를 처리하여 반환하는 메서드
     * age 필드의 숫자 부분을 추출하고 '년생'이라는 문자열을 추가하여 반환합니다.
     *
     * @param age 처리할 age 필드 값
     * @return 처리된 age 문자열
     */
    private String processAge(String age) {
        if (age != null && age.length() >= 4) {
            return age.substring(0, 4)+"년생";
        } else {
            return "정보없음";
        }
    }

    /**
     * sex_cd 필드를 처리하여 반환하는 메서드
     * 'M'은 "남아", 'F'는 "여아", 'Q'는 "성별 추정 어려움",로 반환합니다.
     * 그 외의 값은 "알 수 없음"으로 반환합니다.
     *
     * @param sexCd 처리할 sex_cd 필드 값
     * @return 처리된 성별 문자열
     */
    private String processSexCd(String sexCd) {
        switch (sexCd) {
            case "M":
                return "남아";
            case "F":
                return "여아";
            case "Q":
                return "알 수 없음";
            default:
                return "알 수 없음";
        }
    }

    /**
     * neuter_yn 필드를 처리하여 반환하는 메서드
     * 'N'은 "중성화 안됨", 'Y'는 "중성화 완료", 'U'는 "중성화 추정 어려움"으로 반환합니다.
     * 그 외의 값은 "알 수 없음"으로 반환합니다.
     *
     * @param neuterYn 처리할 neuter_yn 필드 값
     * @return 처리된 중성화 여부 문자열
     */
    private String processNeuterYn(String neuterYn) {
        switch (neuterYn) {
            case "N":
                return "중성화 안됨";
            case "Y":
                return "중성화 완료";
            case "U":
                return "알 수 없음";
            default:
                return "알 수 없음";
        }
    }

    /**
     * 위치 정보 반환하는 메서드
     * pet_info 테이블의 care_nm과 shelter_info테이블의 care_nm이 같은 shelter_info의 org_nm빈환
     *
     * @param careNm pet_info테이블의 care_nm 필드값
     * @return 매칭된 shelter_info의 org_nm 값 또는 정보 없음 문자열 반환*/
    private String processLocation(String careNm) {
        return shelterInfoRepository.findByCareNm(careNm)
                .map(ShelterInfo::getOrgNm)
                .orElse("정보 없음");
    }
}
