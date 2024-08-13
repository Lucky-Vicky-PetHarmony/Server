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

/**
 * OpenAiServiceImpl 클래스는 OpenAiService 인터페이스를 구현하여
 * OpenAI API와의 통신을 처리하는 구체적인 서비스 구현체
 */
@Service
public class OpenAiServiceImpl implements OpenAiService {

    // OpenAI API 엔드포인트 URL. 특정 엔진을 사용하여 텍스트 완성 작업을 수행
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/engines/davinci/completions";

    // Dotenv를 사용하여 .env 파일에서 환경 변수를 로드
    private final Dotenv dotenv = Dotenv.configure()
            .directory("C:/Users/didek/openai-chatbot")
            .filename(".env") // .env 파일 이름
            .load();

    // .env 파일에서 API 키를 로드하여 보안적으로 안전하게 관리
    private final String openAiApiKey = dotenv.get("OPENAI_API_KEY");

    /**
     * analyzeSpecialMark 메서드는 주어진 특수 문구(specialMark)를 OpenAI API를 통해 분석하고,
     * 그 결과를 텍스트로 반환
     *
     * @param specialMark 분석할 텍스트 입력
     * @return OpenAI API가 반환한 분석 결과 텍스트
     * @throws RuntimeException OpenAI API 응답을 파싱할 수 없는 경우 발생
     */
    @Override
    public String analyzeSpecialMark(String specialMark) {
        // RestTemplate 객체를 생성하여 HTTP 요청을 수행
        RestTemplate restTemplate = new RestTemplate();

        // HTTP 요청의 헤더를 설정합니다.
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey); // OpenAI API 키를 사용하여 인증
        headers.set("Content-Type", "application/json"); // 요청 본문이 JSON 형식임을 지정

        // 요청 본문을 JSON 형식으로 작성
        String requestBody = String.format(
                "{\"prompt\": \"Analyze the following special mark and provide relevant characteristics: '%s'.\", \"max_tokens\": 50}",
                specialMark
        );

        // 요청 엔터티를 생성하여 헤더와 본문을 포함
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // OpenAI API로 HTTP POST 요청을 보내고, 응답을 받아옴
        ResponseEntity<String> response = restTemplate.exchange(
                OPENAI_API_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        try {
            // 응답 데이터를 JSON 형식으로 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());

            // 응답에서 'text' 필드를 추출하여 반환
            return root.path("choices").path(0).path("text").asText();
        } catch (Exception e) {
            // 예외가 발생할 경우 RuntimeException으로 래핑하여 처리
            throw new RuntimeException("Failed to parse OpenAI API response", e);
        }
    }
}
