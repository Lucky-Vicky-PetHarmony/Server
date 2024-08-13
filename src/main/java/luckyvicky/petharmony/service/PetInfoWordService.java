package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.WordClassificationDTO;
import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.repository.PetInfoRepository;
import luckyvicky.petharmony.repository.WordRepository;
import luckyvicky.petharmony.service.openapi.OpenAiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PetInfoWordService 클래스는 PetInfo 엔티티를 처리하고, 해당 데이터를 WordClassificationDTO로 변환하는 로직
 * OpenAI API를 사용하여 반려동물의 특성(specialMark)을 분석하고, 이를 기반으로 Word 엔티티와 연관된 정보를 업데이트
 */
@Service
public class PetInfoWordService {

    private final PetInfoRepository petInfoRepository;
    private final WordRepository wordRepository;
    private final OpenAiService openAiService;

    private static final int PAGE_SIZE = 100; // 페이징 처리시 사용

    /**
     * PetInfoWordService의 생성자
     * @param petInfoRepository  PetInfo 엔티티와 상호작용하는 리포지토리
     * @param wordRepository     Word 엔티티와 상호작용하는 리포지토리
     * @param openAiService      OpenAI API와 통신하는 서비스
     */
    public PetInfoWordService(PetInfoRepository petInfoRepository, WordRepository wordRepository, OpenAiService openAiService) {
        this.petInfoRepository = petInfoRepository;
        this.wordRepository = wordRepository;
        this.openAiService = openAiService;
    }

    /**
     * 모든 PetInfo 레코드를 처리
     * 페이징을 사용하여 한 번에 PAGE_SIZE만큼의 레코드를 처리하며,
     * 각 레코드에 대해 processPetInfo 메서드를 호출하여 처리
     */
    @Transactional
    public void processAllPetInfo() {
        int page = 0;
        Page<PetInfo> resultPage;

        // 페이징을 사용하여 모든 PetInfo 레코드를 처리
        do {
            resultPage = petInfoRepository.findAll(PageRequest.of(page, PAGE_SIZE));
            List<PetInfo> petInfoList = resultPage.getContent();
            petInfoList.forEach(this::processPetInfo);
            page++;
        } while (resultPage.hasNext());
    }

    /**
     * 단일 PetInfo 레코드를 처리
     * 이 메서드는 PetInfo 레코드를 WordClassificationDTO로 변환한 다음,
     * OpenAI API를 호출하여 특성을 분석하고, 분석된 결과에 따라 PetInfo 엔티티를 업데이트
     * 최종적으로 PetInfo 레코드를 데이터베이스에 저장
     *
     * @param petInfo 처리할 PetInfo 엔티티
     */
    @Transactional
    public void processPetInfo(PetInfo petInfo) {
        // PetInfo 엔티티를 DTO로 변환
        WordClassificationDTO dto = convertToDTO(petInfo);

        // 특성(specialMark)을 분석하여 관련된 WordId를 결정
        String analyzedWordId = analyzeSpecialMark(dto);
        dto.setWordId(analyzedWordId);

        // DTO에서 엔티티 업데이트 로직 수행
        dto.updateEntity(petInfo);

        // 업데이트된 PetInfo 엔티티를 데이터베이스에 저장
        petInfoRepository.save(petInfo);
    }

    /**
     * PetInfo 엔티티를 WordClassificationDTO로 변환
     * @param petInfo 변환할 PetInfo 엔티티
     * @return        변환된 WordClassificationDTO 객체
     */
    private WordClassificationDTO convertToDTO(PetInfo petInfo) {
        return new WordClassificationDTO(
                petInfo.getDesertionNo(),
                petInfo.getSpecialMark(),
                petInfo.getAge(),
                petInfo.getSexCd(),
                petInfo.getWordId() != null ? String.valueOf(petInfo.getWordId()) : null
        );
    }

    /**
     * OpenAI API를 호출하여 specialMark 필드를 분석하고, 분석 결과에 따라 WordId를 반환
     * 분석 결과는 특정 조건에 따라 다양한 WordId로 매핑
     *
     * @param dto WordClassificationDTO 객체, 분석할 specialMark 필드를 포함
     * @return    분석 결과에 따라 선택된 WordId들 중 최대 5개의 단어 ID를 콤마로 연결한 문자열
     */
    private String analyzeSpecialMark(WordClassificationDTO dto) {
        List<String> wordIds = new ArrayList<>();

        // 나이에 따른 분류
        List<String> recentYears = Arrays.asList("2024", "2023", "2022", "2021", "2020", "2019", "2018");
        if (dto.getAge().contains("60일미만") || recentYears.stream().anyMatch(dto.getAge()::contains)) {
            wordIds.add("5"); // 활발한 (2018년 이후)
        } else {
            wordIds.add("6"); // 차분한 (2017년 이전)
        }

        // 성별에 따른 분류
        switch (dto.getSexCd()) {
            case "M":
                wordIds.add("11"); // 예쁜
                break;
            case "Q":
                wordIds.add("12"); // 귀여운
                break;
            case "F":
                wordIds.add("13"); // 멋진
                break;
        }

        // special_mark 필드를 OpenAiService를 통해 분석
        String openAiAnalysis = openAiService.analyzeSpecialMark(dto.getSpecialMark());

        // 분석된 결과에 따라 WordId 추가
        if (openAiAnalysis.contains("특별한")) {
            wordIds.add("17"); // 특별한
        }
        if (openAiAnalysis.contains("내성적인")) {
            wordIds.add("10"); // 내성적인
        }
        if (openAiAnalysis.contains("순함")) {
            wordIds.add("7"); // 순함
        }
        if (openAiAnalysis.contains("건강한")) {
            wordIds.add("1"); // 건강한
        }
        if (openAiAnalysis.contains("약한")) {
            wordIds.add("2"); // 약한
        }
        if (openAiAnalysis.contains("회복중인")) {
            wordIds.add("3"); // 회복중인
        }
        if (openAiAnalysis.contains("튼튼한")) {
            wordIds.add("4"); // 튼튼한
        }
        if (openAiAnalysis.contains("호기심많음")) {
            wordIds.add("8"); // 호기심많음
        }
        if (openAiAnalysis.contains("사교적인")) {
            wordIds.add("9"); // 사교적인
        }
        if (openAiAnalysis.contains("평범한")) {
            wordIds.add("14"); // 평범한
        }
        if (openAiAnalysis.contains("사랑스러운")) {
            wordIds.add("15"); // 사랑스러운
        }
        if (openAiAnalysis.contains("조용한")) {
            wordIds.add("16"); // 조용한
        }
        if (openAiAnalysis.contains("독특한")) {
            wordIds.add("18"); // 독특한
        }
        if (openAiAnalysis.contains("일반적인")) {
            wordIds.add("19"); // 일반적인
        }
        if (openAiAnalysis.contains("눈에띄는")) {
            wordIds.add("20"); // 눈에띄는
        }

        // 중복된 단어를 제거하고 최대 5개의 단어만 선택하여 반환
        return wordIds.stream().distinct().limit(5).collect(Collectors.joining(","));
    }
}
