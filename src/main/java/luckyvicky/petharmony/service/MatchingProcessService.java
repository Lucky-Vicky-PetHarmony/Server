package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.PetInfo;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * MatchingProcessService는 매칭된 PetInfo 객체를 처리하여 원하는 형식으로 반환하는 서비스입니다.
 */
@Service
public class MatchingProcessService {

    /**
     * 주어진 PetInfo 객체를 처리하여 원하는 형식의 데이터를 반환합니다.
     *
     * @param petInfo 처리할 PetInfo 객체
     * @return 처리된 데이터를 담고 있는 Map 객체
     */
    public Map<String, Object> processPetInfo(PetInfo petInfo) {
        Map<String, Object> result = new HashMap<>();

        // words 필드에서 랜덤으로 3개의 단어를 선택하여 반환
        List<String> wordsList = Arrays.asList(petInfo.getWords().split(","));
        Collections.shuffle(wordsList); // 단어 리스트를 무작위로 섞음
        result.put("words", wordsList.stream().limit(3).collect(Collectors.toList())); // 최대 3개의 단어만 선택하여 결과에 추가

        // kind_cd 필드를 처리하여 반환
        result.put("kind_cd", processKindCd(petInfo.getKindCd()));

        // age 필드를 처리하여 반환
        result.put("age", processAge(petInfo.getAge()));

        // sex_cd 필드를 처리하여 반환
        result.put("sex_cd", processSexCd(petInfo.getSexCd()));

        // neuter_yn 필드를 처리하여 반환
        result.put("neuter_yn", processNeuterYn(petInfo.getNeuterYn()));

        return result; // 최종 처리된 데이터를 Map으로 반환
    }

    /**
     * kind_cd 필드를 처리하여 반환하는 메서드
     * '기타축종'이라는 단어가 포함된 경우 대괄호([]) 바깥의 값을 반환하고,
     * 포함되지 않은 경우 대괄호 안의 값을 반환합니다.
     *
     * @param kindCd 처리할 kind_cd 필드 값
     * @return 처리된 kind_cd 문자열
     */
    private String processKindCd(String kindCd) {
        if (kindCd.contains("기타축종")) {
            return kindCd.replaceAll("\\[.*?\\]", "").trim(); // 대괄호 안의 내용을 제거하고 반환
        } else {
            return kindCd.replaceAll(".*?\\[(.*?)\\]", "$1").trim(); // 대괄호 안의 내용을 추출하여 반환
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
        String numericPart = age.replaceAll("\\D", ""); // 숫자만 추출
        return numericPart + "년생"; // 추출된 숫자에 '년생'을 추가하여 반환
    }

    /**
     * sex_cd 필드를 처리하여 반환하는 메서드
     * 'Q'는 "성별 추정 어려움", 'F'는 "여아", 'M'은 "남아"로 반환합니다.
     * 그 외의 값은 "알 수 없음"으로 반환합니다.
     *
     * @param sexCd 처리할 sex_cd 필드 값
     * @return 처리된 성별 문자열
     */
    private String processSexCd(String sexCd) {
        switch (sexCd) {
            case "Q":
                return "알 수 없음";
            case "F":
                return "여아";
            case "M":
                return "남아";
            default:
                return "알 수 없음";
        }
    }

    /**
     * neuter_yn 필드를 처리하여 반환하는 메서드
     * 'U'는 "중성화 추정 어려움", 'Y'는 "중성화 완료", 'N'은 "중성화 안됨"으로 반환합니다.
     * 그 외의 값은 "알 수 없음"으로 반환합니다.
     *
     * @param neuterYn 처리할 neuter_yn 필드 값
     * @return 처리된 중성화 여부 문자열
     */
    private String processNeuterYn(String neuterYn) {
        switch (neuterYn) {
            case "U":
                return "중성화 추정 어려움";
            case "Y":
                return "중성화 완료";
            case "N":
                return "알 수 없음";
            default:
                return "알 수 없음";
        }
    }
}
