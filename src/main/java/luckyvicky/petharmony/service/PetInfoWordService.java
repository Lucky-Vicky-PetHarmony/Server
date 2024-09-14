package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.WordClassificationDTO;
import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.PetInfoWord;
import luckyvicky.petharmony.entity.Word;
import luckyvicky.petharmony.repository.PetInfoRepository;
import luckyvicky.petharmony.repository.PetInfoWordRepository;
import luckyvicky.petharmony.repository.WordRepository;
import luckyvicky.petharmony.service.openapi.OpenAiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetInfoWordService {

    private final PetInfoRepository petInfoRepository;
    private final PetInfoWordRepository petInfoWordRepository;
    private final WordRepository wordRepository;
    private final OpenAiService openAiService;

    public PetInfoWordService(PetInfoRepository petInfoRepository,
                              PetInfoWordRepository petInfoWordRepository,
                              WordRepository wordRepository,
                              OpenAiService openAiService) {
        this.petInfoRepository = petInfoRepository;
        this.petInfoWordRepository = petInfoWordRepository;
        this.wordRepository = wordRepository;
        this.openAiService = openAiService;
    }

    // 하나의 WordClassificationDTO를 처리하는 메서드
    @Transactional
    public void processSinglePetInfo(WordClassificationDTO dto) {

        // PetInfo를 desertionNo로 가져옴
        PetInfo petInfo = petInfoRepository.findByDesertionNo(dto.getDesertionNo());
        if (petInfo == null) {
            throw new IllegalArgumentException("PetInfo not found for desertionNo: " + dto.getDesertionNo());
        }

        // 이미 저장된 데이터가 있는지 확인
        List<PetInfoWord> existingWords = petInfoWordRepository.findByDesertionNo(dto.getDesertionNo());
        if (!existingWords.isEmpty()) {
            return;  // 이미 저장된 데이터가 있으면 분석하지 않음
        }

        // specialMark 필드를 분석하여 관련된 Word 엔티티 목록 가져옴
        List<Word> words = analyzeSpecialMark(dto);

        // 분석된 결과를 저장
        List<PetInfoWord> petInfoWords = words.stream()
                .limit(5)
                .map(word -> PetInfoWord.builder()
                        .petInfo(petInfo)
                        .word(word)
                        .build())
                .collect(Collectors.toList());

        // PetInfoWord 엔티티 저장
        petInfoWordRepository.saveAll(petInfoWords);
    }

    // OpenAI API를 통해 specialMark 분석
    private List<Word> analyzeSpecialMark(WordClassificationDTO dto) {
        String openAiAnalysis = openAiService.analyzeSpecialMark(dto.getSpecialMark());
        List<String> wordIds = new ArrayList<>();

        // OpenAI API 결과를 기반으로 Word 엔티티 가져오기
        if (openAiAnalysis != null && !openAiAnalysis.isEmpty()) {
            if (openAiAnalysis.contains("건강한")) wordIds.add("1");
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

        // Word 엔티티 반환
        return wordIds.stream()
                .map(wordId -> wordRepository.findById(Long.parseLong(wordId)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
