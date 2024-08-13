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

    // OpenAI API 엔드포인트 URL
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/engines/davinci/completions";

    // .env 파일에서 API 키를 로드하여 보안적으로 안전하게 관리
    private final Dotenv dotenv = Dotenv.configure()
            .directory("C:/Users/didek/openai-chatbot")
            .filename(".env")
            .load();

    private final String openAiApiKey = dotenv.get("OPENAI_API_KEY");

    private final RestTemplate restTemplate;

    // 생성자를 통해 RestTemplate을 주입받아 재사용
    public OpenAiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String analyzeSpecialMark(String specialMark) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey); // OpenAI API 인증
        headers.set("Content-Type", "application/json"); // 요청 본문이 JSON 형식임을 지정

        // 요청 본문을 JSON 형식으로 작성
        String requestBody = String.format(
                "{\"prompt\": \"Analyze the following special mark and provide relevant characteristics: '%s'.\", \"max_tokens\": 50}",
                specialMark
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

            return root.path("choices").path(0).path("text").asText();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("OpenAI API returned an error: " + e.getStatusCode() + " " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to connect to OpenAI API", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse OpenAI API response", e);
        }
    }
}
