package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.WordClassificationDTO;
import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.repository.PetInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetInfoWordService {

    @Autowired
    private PetInfoRepository petInfoRepository;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/engines/davinci/completions";
    private static final String OPENAI_API_KEY = ""; // OpenAI API 키를 입력하세요

    // 모든 pet_info 데이터를 처리하는 메서드
    public void processAllPetInfo() {
        int page = 0;
        int size = 100; // 한 번에 처리할 데이터 양 설정

        Page<PetInfo> resultPage;

        do {
            // 페이징을 통해 데이터를 가져옴
            resultPage = petInfoRepository.findAll(PageRequest.of(page, size));
            List<PetInfo> petInfoList = resultPage.getContent();
            petInfoList.forEach(this::processPetInfo); // 각 pet_info 데이터를 처리
            page++;
        } while (resultPage.hasNext());
    }

    // 각 pet_info 데이터를 처리하는 메서드
    public void processPetInfo(PetInfo petInfo) {
        // PetInfo 엔티티를 DTO로 변환
        WordClassificationDTO dto = convertToDTO(petInfo);

        // 단어 분석 로직을 호출하여 wordId 설정
        String analyzedWordId = analyzeSpecialMark(dto);
        dto.setWordId(analyzedWordId);

        // DTO를 사용하여 엔티티를 업데이트
        dto.toEntity(petInfo);

        // 엔티티를 데이터베이스에 저장
        petInfoRepository.save(petInfo);
    }

    // PetInfo 엔티티를 WordClassificationDTO로 변환하는 메서드
    private WordClassificationDTO convertToDTO(PetInfo petInfo) {
        return new WordClassificationDTO(
                petInfo.getDesertionNo(),
                petInfo.getSpecialMark(),
                petInfo.getAge(),
                String.valueOf(petInfo.getSexCd()),
                petInfo.getWordId()
        );
    }

    // WordClassificationDTO를 분석하여 WordId를 설정하는 메서드
    private String analyzeSpecialMark(WordClassificationDTO dto) {
        List<String> wordIds = new ArrayList<>();

        // 나이에 따른 분류 (String 비교로 연도 구분)
        List<String> recentYears = Arrays.asList("2024", "2023", "2022", "2021", "2020", "2019", "2018");

        // age 필드에 따라 "활발한" 또는 "차분한"을 분류
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

        // special_mark 필드를 분석 (OpenAI API 호출)
        String openAiAnalysis = callOpenAiApi(dto.getSpecialMark());

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

    // OpenAI API를 호출하여 special_mark를 분석하는 메서드
    private String callOpenAiApi(String specialMark) {
        RestTemplate restTemplate = new RestTemplate();

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + OPENAI_API_KEY); // OpenAI API 키 설정
        headers.set("Content-Type", "application/json"); // 콘텐츠 유형 설정

        // 요청 본문 설정
        String requestBody = String.format(
                "{\"prompt\": \"Analyze the following special mark and provide relevant characteristics: '%s'.\", \"max_tokens\": 50}",
                specialMark
        );

        // HTTP 요청 엔터티 생성
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // OpenAI API 호출 및 응답 받기
        ResponseEntity<String> response = restTemplate.exchange(
                OPENAI_API_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        // 응답을 단순화하여 문자열로 반환
        return response.getBody();
    }
}
