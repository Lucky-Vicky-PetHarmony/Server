package luckyvicky.petharmony.service.openapi;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

/**
 * OpenAiServiceImpl 클래스는 OpenAiService 인터페이스를 구현하여
 * OpenAI API와의 통신을 처리하는 구체적인 서비스 구현체
 */
@Service
public class OpenAiServiceImpl implements OpenAiService {

    // OpenAI API의 엔드포인트 URL
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    // RestTemplate은 HTTP 요청을 보내기 위함
    private final RestTemplate restTemplate;

    // 실제 환경 변수 로딩을 위한 Dotenv 객체
    private Dotenv dotenv;

    // 스프링 환경 변수로 경로를 주입 받습니다.
    @Value("${dotenv.filepath}")
    private String dotenvFilePath;

    public OpenAiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // @PostConstruct를 이용해 Dotenv 초기화
    @PostConstruct
    private void initializeDotenv() {
        if (dotenvFilePath != null && !dotenvFilePath.isEmpty()) {
            this.dotenv = Dotenv.configure()
                    .directory("C:\\Users\\didek\\openai-chatbot")
                    .filename(".env")
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();
        } else {
            this.dotenv = Dotenv.configure()
                    .directory(dotenvFilePath)
                    .filename(".env")
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();
        }
    }

    // 환경 변수에서 API 키를 불러옴
    private String getOpenAiApiKey() {
        return dotenv.get("OPENAI_API_KEY", "default-api-key");
    }

    /**
     * analyzeSpecialMark 메서드는 특이사항 문자열을 분석하여
     * OpenAI API를 통해 분석된 결과를 반환
     * @param specialMark 분석할 특이사항 문자열
     * @return 분석된 결과 문자열
     */
    @Override
    public String analyzeSpecialMark(String specialMark) {
        // HTTP 요청 헤더를 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getOpenAiApiKey());
        headers.set("Content-Type", "application/json");

        // 특이사항 문자열을 온점(.) 또는 쉼표(,)로 분리하여 배열로 만든다
        String[] sentences = specialMark.split("\\.|,\\s*");

        StringBuilder combinedResponse = new StringBuilder();

        for (String sentence : sentences) {
            if (sentence.trim().isEmpty()) {
                continue;
            }

            String requestBody = String.format(
                    "{\"model\": \"gpt-4o-mini\", \"messages\": [{\"role\": \"user\", \"content\": " +
                            "\"단, 다음의 단어가 포함 되어있는 경우 분석하지말고 매칭된 키워드로 바로 반환" +
                            "\\\"건강한, 회복중인, 온순한, 사나운, 활발한, 차분한, 겁많은, 호기심많은, 사교적인, 내성적인, 예쁜, 돌봄이 필요한, 멋진, 평범한, 순종적인, 독립적인, 특별한, 독특한, 일반적인, 윤기나는\\\" " +
                            "특이사항 분석 후 매칭된 단어 반환: '%s'.\"}], \"max_tokens\": 50}",
                    sentence.trim()
            );

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        OPENAI_API_URL,
                        HttpMethod.POST,
                        entity,
                        String.class
                );

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(response.getBody());
                String analysisResult = root.path("choices").path(0).path("message").path("content").asText();
                String categorizedResult = categorizeResponse(analysisResult);
                combinedResponse.append(categorizedResult).append(" ");
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    System.err.println("Unauthorized: Invalid API key or token. Continuing without processing.");
                } else {
                    throw new RuntimeException("OpenAI API 요청 오류: " + e.getStatusCode() + " " + e.getResponseBodyAsString(), e);
                }
            } catch (HttpServerErrorException e) {
                throw new RuntimeException("OpenAI API 서버 오류: " + e.getStatusCode() + " " + e.getResponseBodyAsString(), e);
            } catch (RestClientException e) {
                throw new RuntimeException("OpenAI API 연결 실패", e);
            } catch (Exception e) {
                throw new RuntimeException("OpenAI API 응답 파싱 실패", e);
            }
        }

        return combinedResponse.toString().trim();
    }

    /**
     * categorizeResponse 메서드는 OpenAI의 응답을 분석하여
     * 미리 정의된 카테고리로 매핑
     * @param response OpenAI로부터 받은 응답 문자열
     * @return 카테고리화된 결과 문자열
     */
    private String categorizeResponse(String response) {
        // 응답에서 특정 키워드를 찾아서 카테고리로 매핑
        if (response.contains("크레스티드 게코")) {
            return "독특한";
        } else if (response.contains("외상안보임") || response.contains("외상없음") || response.contains("외상 없음")) {
            return "건강한";
        } else if (response.contains("털엄킴") || response.contains("엉킴")) {
            return "돌봄이 필요한";
        } else if (response.contains("털상태양호")) {
            return "윤기나는";
        } else if (response.contains("몸무게 추정") || response.contains("몸무게 추정") || response.contains("색") || response.contains("단미") || response.contains("털때탐")) {
            return null;
        }
        // 추가적인 카테고리 매핑 로직을 작성
        return response;
    }
}
