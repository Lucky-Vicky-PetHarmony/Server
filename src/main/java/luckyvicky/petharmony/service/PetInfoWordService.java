package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.WordClassificationDTO;
import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.repository.PetInfoRepository;
import luckyvicky.petharmony.service.openapi.OpenAiService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PetInfoWordService 클래스는 PetInfo 엔티티를 처리하고, 해당 데이터를 WordClassificationDTO로 변환한 후
 * OpenAI API를 사용하여 반려동물의 특성(specialMark)을 분석하고, 이를 기반으로 관련된 단어 정보를 업데이트합니다.
 */
@Service
public class PetInfoWordService {

    private final PetInfoRepository petInfoRepository;
    private final OpenAiService openAiService;

    private static final int PAGE_SIZE = 100; // 페이징 처리 시 사용

    /**
     * PetInfoWordService의 생성자
     *
     * @param petInfoRepository PetInfo 엔티티와 상호작용하는 리포지토리
     * @param openAiService     OpenAI API와 통신하는 서비스
     */
    public PetInfoWordService(PetInfoRepository petInfoRepository, OpenAiService openAiService) {
        this.petInfoRepository = petInfoRepository;
        this.openAiService = openAiService;
    }

    // 기존 코드는 주석처리하여 비활성화
    /*
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
    */

    /**
     * 상위 5개의 PetInfo 레코드를 처리합니다.
     */
    @Transactional
    public void processTop5PetInfo() {
        // 상위 5개의 PetInfo 데이터를 가져옴
        List<PetInfo> top5PetInfoList = petInfoRepository.findAll(PageRequest.of(0, 50)).getContent();

        for (PetInfo petInfo : top5PetInfoList) {
            // desertionNo가 null인 경우 스킵
            if (petInfo.getDesertionNo() == null) {
                continue;
            }

            // PetInfo 엔티티를 분석하고 업데이트
            processPetInfo(petInfo);

            // 결과 출력
            //System.out.println("저장된 words 필드 값 (desertionNo: " + petInfo.getDesertionNo() + "): " + petInfo.getWords());
        }
    }

    /**
     * 단일 PetInfo 레코드를 처리합니다.
     * 이 메서드는 PetInfo 레코드를 WordClassificationDTO로 변환한 다음,
     * OpenAI API를 호출하여 특성을 분석하고, 분석된 결과에 따라 PetInfo 엔티티를 업데이트합니다.
     * 최종적으로 PetInfo 레코드를 데이터베이스에 저장합니다.
     *
     * @param petInfo 처리할 PetInfo 엔티티
     */
    @Transactional
    public void processPetInfo(PetInfo petInfo) {
        // desertionNo가 null인 경우 메서드를 종료
        if (petInfo.getDesertionNo() == null) {
            return;
        }

        // words 열에 이미 데이터가 있는 경우, OpenAI 호출을 건너뜀
        if (petInfo.getWords() != null && !petInfo.getWords().isEmpty()) {
            //System.out.println("words 필드에 이미 데이터가 있습니다. OpenAI 호출을 건너뜁니다.");
            return;
        }

        // PetInfo 엔티티를 DTO로 변환
        WordClassificationDTO dto = convertToDTO(petInfo);

        // 특성(specialMark)을 분석하여 관련된 Words를 결정
        String analyzedWords = analyzeSpecialMark(dto);
        dto.setWords(analyzedWords);

        // DTO에서 엔티티 업데이트 로직 수행
        dto.updateEntity(petInfo);

        // 업데이트된 PetInfo 엔티티를 데이터베이스에 저장
        petInfoRepository.save(petInfo);
    }

    /**
     * PetInfo 엔티티를 WordClassificationDTO로 변환합니다.
     *
     * @param petInfo 변환할 PetInfo 엔티티
     * @return 변환된 WordClassificationDTO 객체
     */
    private WordClassificationDTO convertToDTO(PetInfo petInfo) {
        return new WordClassificationDTO(
                petInfo.getDesertionNo(),
                petInfo.getSpecialMark(),
                petInfo.getAge(),
                petInfo.getSexCd(),
                petInfo.getWords() // Words 필드를 직접 가져옴
        );
    }

    /**
     * OpenAI API를 호출하여 specialMark 필드를 분석하고, 분석 결과에 따라 Words를 반환합니다.
     * 분석 결과는 특정 조건에 따라 다양한 Words로 매핑됩니다.
     *
     * @param dto WordClassificationDTO 객체, 분석할 specialMark 필드를 포함
     * @return 분석 결과에 따라 선택된 Words들 중 최대 5개의 단어 ID를 콤마로 연결한 문자열
     */
    private String analyzeSpecialMark(WordClassificationDTO dto) {
        List<String> wordIds = new ArrayList<>();

        // special_mark 필드를 OpenAiService를 통해 분석
        String specialMark = dto.getSpecialMark();
        if (specialMark != null && !specialMark.isEmpty()) {
            String openAiAnalysis = openAiService.analyzeSpecialMark(specialMark);

            if (openAiAnalysis != null && !openAiAnalysis.isEmpty()) {
                // 분석된 결과에 따라 Words 추가
                if (openAiAnalysis.contains("건강한")) {
                    wordIds.add("1");
                }
                if (openAiAnalysis.contains("회복중인")) {
                    wordIds.add("2");
                }
                if (openAiAnalysis.contains("온순한")) {
                    wordIds.add("3");
                }
                if (openAiAnalysis.contains("튼튼한")) {
                    wordIds.add("4");
                }
                if (openAiAnalysis.contains("활발한")) {
                    wordIds.add("5");
                }
                if (openAiAnalysis.contains("차분한")) {
                    wordIds.add("6");
                }
                if (openAiAnalysis.contains("겁많은")) {
                    wordIds.add("7");
                }
                if (openAiAnalysis.contains("호기심많은")) {
                    wordIds.add("8");
                }
                if (openAiAnalysis.contains("사교적인")) {
                    wordIds.add("9");
                }
                if (openAiAnalysis.contains("내성적인")) {
                    wordIds.add("10");
                }
                if (openAiAnalysis.contains("예쁜")) {
                    wordIds.add("11");
                }
                if (openAiAnalysis.contains("돌봄이 필요한")) {
                    wordIds.add("12");
                }
                if (openAiAnalysis.contains("평범한")) {
                    wordIds.add("13");
                }
                if (openAiAnalysis.contains("멋진")) {
                    wordIds.add("14");
                }
                if (openAiAnalysis.contains("순종적인")) {
                    wordIds.add("15");
                }
                if (openAiAnalysis.contains("독립적인")) {
                    wordIds.add("16");
                }
                if (openAiAnalysis.contains("특별한")) {
                    wordIds.add("17");
                }
                if (openAiAnalysis.contains("독특한")) {
                    wordIds.add("18");
                }
                if (openAiAnalysis.contains("일반적인")) {
                    wordIds.add("19");
                }
                if (openAiAnalysis.contains("윤기나는")) {
                    wordIds.add("20");
                }
            }
        }

        // 중복된 단어를 제거하고 최대 10개의 단어만 선택하여 반환
        return wordIds.stream().distinct().limit(10).collect(Collectors.joining(","));
    }
}
