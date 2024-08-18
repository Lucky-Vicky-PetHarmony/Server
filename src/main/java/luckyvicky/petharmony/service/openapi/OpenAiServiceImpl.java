package luckyvicky.petharmony.service.openapi;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

/**
 * OpenAiServiceImpl 클래스는 OpenAiService 인터페이스를 구현하여
 * OpenAI API와의 통신을 처리하는 구체적인 서비스 구현체입니다.
 */
@Service
public class OpenAiServiceImpl implements OpenAiService {

    // OpenAI API의 엔드포인트 URL
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    // 환경 변수를 사용하여 API 키를 불러옵니다.
    private final Dotenv dotenv = Dotenv.configure()
            .directory("C:/Users/didek/openai-chatbot")
            .filename(".env")
            .load();

    // 환경 변수에서 API 키를 불러옵니다.
    private final String openAiApiKey = dotenv.get("OPENAI_API_KEY");

    // RestTemplate은 HTTP 요청을 보내기 위한 스프링의 편리한 도구입니다.
    private final RestTemplate restTemplate;

    // 생성자 주입을 통해 RestTemplate을 주입받습니다.
    public OpenAiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * analyzeSpecialMark 메서드는 특이사항 문자열을 분석하여
     * OpenAI API를 통해 분석된 결과를 반환합니다.
     * @param specialMark 분석할 특이사항 문자열
     * @return 분석된 결과 문자열
     */
    @Override
    public String analyzeSpecialMark(String specialMark) {
        // HTTP 요청 헤더를 설정합니다.
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey); // OpenAI API 인증을 위한 Bearer 토큰 설정
        headers.set("Content-Type", "application/json"); // 요청 본문이 JSON 형식임을 지정

        // 특이사항 문자열을 온점(.) 또는 쉼표(,)로 분리하여 배열로 만듭니다.
        String[] sentences = specialMark.split("\\.|,\\s*");

        // 각 문장의 분석 결과를 결합하기 위한 StringBuilder 객체를 생성합니다.
        StringBuilder combinedResponse = new StringBuilder();

        // 각 문장을 순회하며 OpenAI API를 통해 분석합니다.
        for (String sentence : sentences) {
            if (sentence.trim().isEmpty()) {
                continue;  // 빈 문장은 건너뜁니다.
            }

            // 요청 본문을 JSON 형식으로 작성합니다.
            String requestBody = String.format(
                    "{\"model\": \"gpt-4\", \"messages\": [{\"role\": \"user\", \"content\": " +
                            "\"제공하는 특이사항을 분석하여 다음의 단어들 중 연관되는 것을 매칭하여 이유와 함께 제공하라. " +
                            "단, 다음의 단어가 포함 되어있는 경우 분석하지말고 매칭된 키워드로 바로 반환하라" +
                            "\\\"건강한, 회복중인, 온순한, 사나운, 활발한, 차분한, 겁많은, 호기심많은, 사교적인, 내성적인, 예쁜, 돌봄이 필요한, 멋진, 평범한, 순종적인, 독립적인, 특별한, 독특한, 일반적인, 윤기나는\\\" " +
                            "특이사항 분석 후 매칭된 단어를 이유와 함께 제공하세요: '%s'.\"}], \"max_tokens\": 50}",
                    sentence.trim()
            );




            // HTTP 요청 엔터티를 생성합니다.
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            try {
                // OpenAI API로 POST 요청을 보냅니다.
                ResponseEntity<String> response = restTemplate.exchange(
                        OPENAI_API_URL,
                        HttpMethod.POST,
                        entity,
                        String.class
                );

                // 응답 본문을 JSON 형식으로 파싱합니다.
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(response.getBody());

                // 분석된 결과를 추출합니다.
                String analysisResult = root.path("choices").path(0).path("message").path("content").asText();

                // 분석된 결과를 카테고리화하고 결합합니다.
                String categorizedResult = categorizeResponse(analysisResult);
                combinedResponse.append(categorizedResult).append(" ");
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                // HTTP 오류가 발생한 경우 예외를 던집니다.
                throw new RuntimeException("OpenAI API 요청 오류: " + e.getStatusCode() + " " + e.getResponseBodyAsString(), e);
            } catch (RestClientException e) {
                // OpenAI API와의 연결에 실패한 경우 예외를 던집니다.
                throw new RuntimeException("OpenAI API 연결 실패", e);
            } catch (Exception e) {
                // 응답 파싱에 실패한 경우 예외를 던집니다.
                throw new RuntimeException("OpenAI API 응답 파싱 실패", e);
            }
        }

        // 최종 결합된 응답을 반환합니다.
        return combinedResponse.toString().trim();
    }

    /**
     * categorizeResponse 메서드는 OpenAI의 응답을 분석하여
     * 미리 정의된 카테고리로 매핑합니다.
     * @param response OpenAI로부터 받은 응답 문자열
     * @return 카테고리화된 결과 문자열
     */
    private String categorizeResponse(String response) {
        // 응답에서 특정 키워드를 찾아서 카테고리로 매핑합니다.
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
        // 추가적인 카테고리 매핑 로직을 작성할 수 있습니다.
        return response;
    }
}
