package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.Word;
import luckyvicky.petharmony.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DetailAdoptionService 클래스는 PetInfoFormatService를 확장하여 추가적인 기능을 제공하는 서비스
 * 기본적인 PetInfo 포맷팅 기능에 더해, 모든 단어를 반환하고 추가적인 필드를 처리하는 기능을 포함
 */
@Service
public class DetailAdoptionService implements PetInfoFormatter {

    private final PetInfoFormatService petInfoFormatService; // 기본 PetInfo 포맷팅을 담당하는 서비스
    private final WordRepository wordRepository; // Word 엔티티와 관련된 데이터베이스 작업을 처리하는 레포지토리

    /**
     * DetailAdoptionService 생성자.
     * PetInfoFormatService와 WordRepository를 의존성 주입을 통해 초기화
     *
     * @param petInfoFormatService 기본 포맷팅 서비스를 제공하는 PetInfoFormatService
     * @param wordRepository Word 엔티티 관련 데이터베이스 작업을 처리하는 레포지토리
     */
    @Autowired
    public DetailAdoptionService(PetInfoFormatService petInfoFormatService, WordRepository wordRepository) {
        this.petInfoFormatService = petInfoFormatService;
        this.wordRepository = wordRepository;
    }

    /**
     * 주어진 PetInfo 객체를 처리하여 확장된 형식의 데이터를 반환
     * 기본 포맷팅 로직을 사용한 후, 모든 단어를 포함하고 추가 필드를 처리하여 반환
     *
     * @param petInfo 처리할 PetInfo 객체
     * @return 확장된 형식의 데이터를 담고 있는 Map 객체
     */
    @Override
    public Map<String, Object> processPetInfo(PetInfo petInfo) {
        // 기본 포맷팅 로직을 사용하여 결과를 가져옴
        Map<String, Object> result = petInfoFormatService.processPetInfo(petInfo);

        // 모든 단어를 반환하도록 변경된 로직 적용
        result.put("words", processAllWords(petInfo.getWords()));

        // 추가 필드 처리 (예: 색상 코드)
        result.put("color_cd", petInfo.getColorCd());

        // 공고기한, "notice_std ~ notice_edt"
        result.put("notice_period", processDate(petInfo));

        // 발견날짜
        result.put("happen_dt", petInfo.getHappenDt());

        // 발견장소
        result.put("happen_place", petInfo.getHappenPlace());

        // 보호장소
        result.put("care_nm", petInfo.getCareNm());

        // 특이사항
        result.put("special_mark", petInfo.getSpecialMark());

        // 처리된 결과 반환
        return result;
    }

    /**
     * 모든 단어를 반환하는 메서드.
     * 주어진 단어 목록을 파싱하여, 데이터베이스에서 해당 단어의 상세 정보를 가져와 반환
     *
     * @param words 콤마로 구분된 단어 ID 문자열
     * @return 모든 단어의 wordSelect 값을 포함한 리스트
     */
    private List<String> processAllWords(String words) {
        // words 필드가 비었거나 null인 경우 빈 리스트 반환
        if (words == null || words.isEmpty()) {
            return Collections.emptyList();
        }

        // words 문자열을 콤마로 분리하여 Long 타입의 단어 ID 리스트로 변환
        List<Long> wordIds = Arrays.stream(words.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        // 단어 ID 리스트를 사용해 Word 엔티티를 데이터베이스에서 조회
        List<Word> wordEntities = wordRepository.findByWordIdIn(wordIds);

        // 조회된 Word 엔티티에서 wordSelect 필드 값을 추출하여 반환
        return wordEntities.stream()
                .map(Word::getWordSelect)
                .collect(Collectors.toList());
    }

    /**
     * 주어진 축종 정보를 처리하는 메서드.
     * "기타축종"이 포함된 경우 "-"를 반환하고, 대괄호 안의 값을 추출하여 반환
     *
     * @param species 축종 정보 문자열
     * @return 처리된 축종 정보 문자열
     */
    public String processSpecies(String species) {
        // 축종 정보에 "기타축종"이 포함된 경우 "-" 반환
        if (species.contains("기타축종")) {
            return "-";
        }
        // 축종 정보에 대괄호가 포함된 경우 대괄호 안의 값을 반환
        else if (species.contains("[") && species.contains("]")) {
            return species.substring(species.indexOf("]") + 1);
        }

        // 그 외의 경우 원래 축종 정보를 그대로 반환
        return species;
    }

    /**
     * 공고 기한을 "notice_sdt ~ notice_edt"형식으로 변환하는 메서드
     * 공고 시작일과 종료일을 받아서 문자열로 반환
     *
     * @param petInfo 처리할 PetInfo 객체
     * @return 형식화된 공고 기한 문자열
     */
    private String processDate(PetInfo petInfo) {
        Date noticeSdt = petInfo.getNoticeSdt();
        Date noticeEdt = petInfo.getNoticeEdt();
        if (noticeSdt != null && noticeEdt != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(noticeSdt) + " ~ " + sdf.format(noticeEdt);
        }
        return "기간 정보 없음";
    }
}
