package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.WordClassificationDTO;
import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.PetInfoWord;
import luckyvicky.petharmony.entity.Word;
import luckyvicky.petharmony.repository.PetInfoWordRepository;
import luckyvicky.petharmony.repository.WordRepository;
import luckyvicky.petharmony.service.openapi.OpenAiService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * PetInfoWordService 클래스는 PetInfo 엔티티를 처리하고, 해당 데이터를 WordClassificationDTO로 변환한 후
 * OpenAI API를 사용하여 반려동물의 특성(specialMark)을 분석하고, 이를 기반으로 관련된 단어 정보를 업데이트합니다.
 */
@Service
public class PetInfoWordService {

    private final PetInfoWordRepository petInfoWordRepository;
    private final OpenAiService openAiService;
    private final WordRepository wordRepository;

    public PetInfoWordService(PetInfoWordRepository petInfoWordRepository, OpenAiService openAiService, WordRepository wordRepository) {
        this.petInfoWordRepository = petInfoWordRepository;
        this.openAiService = openAiService;
        this.wordRepository = wordRepository;
    }

    /**
     * PetInfo 엔티티를 처리하여 관련된 Word 정보를 업데이트
     *
     * @param petInfo 처리할 PetInfo 엔티티
     */
    @Transactional
    public void processPetInfo(PetInfo petInfo) {
        // desertionNo가 null인 경우 메서드를 종료
        if (petInfo.getDesertionNo() == null) {
            return;
        }

        // 해당 desertionNo로 이미 저장된 단어가 있는지 확인
        Optional<List<PetInfoWord>> existingWords = petInfoWordRepository.findByPetInfo(petInfo);
        if (existingWords.isPresent() && !existingWords.get().isEmpty()) {
            // 이미 저장된 단어가 있으면 분석을 생략
            return;
        }

        // PetInfo 엔티티를 DTO로 변환
        WordClassificationDTO dto = convertToDTO(petInfo);

        // 특성(specialMark)을 분석하여 관련된 word_id를 결정
        List<Word> words = analyzeSpecialMark(dto);

        // 분석된 결과에 따라 PetInfoWord 엔티티를 데이터베이스에 저장
        for (Word word : words) {
            PetInfoWord petInfoWord = PetInfoWord.builder()
                    .petInfo(petInfo)
                    .word(word)
                    .build();
            petInfoWordRepository.save(petInfoWord);
        }
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
                petInfo.getSpecialMark()
        );
    }

    /**
     * OpenAI API를 호출하여 specialMark 필드를 분석하고, 분석 결과에 따라 관련된 Word 엔티티 리스트를 반환
     *
     * @param dto WordClassificationDTO 객체, 분석할 specialMark 필드를 포함
     * @return 분석 결과에 따라 선택된 Word 엔티티 리스트 반환
     */
    private List<Word> analyzeSpecialMark(WordClassificationDTO dto) {
        List<String> wordIds = new ArrayList<>();

        // special_mark 필드를 OpenAiService를 통해 분석
        String specialMark = dto.getSpecialMark();
        if (specialMark != null && !specialMark.isEmpty()) {
            String openAiAnalysis = openAiService.analyzeSpecialMark(specialMark);

            if (openAiAnalysis != null && !openAiAnalysis.isEmpty()) {
                // 분석된 결과에 따라 word_id 추가
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

        // 중복된 단어를 제거하고 최대 5개의 단어만 선택하여 Word 엔티티로 변환하여 반환
        return wordIds.stream()
                .distinct()
                .limit(5)
                .map(wordId -> wordRepository.findById(Long.parseLong(wordId)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
