package luckyvicky.petharmony.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * LocationService 클래스는 주소 정보를 통해 위도와 경도를 얻는 기능을 제공
 * WebClient를 사용하여 OpenStreetMap의 Nominatim API를 통해 주소를 위도와 경도로 변환
 */
@Service
public class LocationService {

    private final WebClient webClient; // HTTP 요청을 비동기적으로 처리하기 위한 WebClient 객체

    /**
     * LocationService 생성자. WebClient.Builder 객체를 주입받아 초기화
     *
     * @param webClientBuilder WebClient.Builder 객체
     */
    public LocationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://nominatim.openstreetmap.org").build();
    }

    /**
     * 주어진 주소를 기반으로 Nominatim API를 호출하여 위도와 경도를 얻는다.
     *
     * @param address 위도와 경도를 얻고자 하는 주소
     * @return 위도와 경도를 포함한 double 배열 (index 0: 위도, index 1: 경도)
     */
    public double[] getLatLonFromAddress(String address) {
        try {
            // Nominatim API의 검색 엔드포인트 URI를 생성
            URI uri = UriComponentsBuilder.fromPath("/search")
                    .queryParam("q", address) // 검색 쿼리에 주소를 추가
                    .queryParam("format", "json") // 응답 포맷을 JSON으로 설정
                    .build().toUri();

            // WebClient를 사용하여 API 요청을 보내고, 응답을 문자열로 수신
            String response = this.webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // 비동기 요청을 동기 방식으로 대기

            // JSON 파서를 사용하여 응답 문자열을 JSON 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);

            // JSON 응답이 배열이고, 하나 이상의 결과가 있는지 확인
            if (root.isArray() && root.size() > 0) {
                // 첫 번째 결과에서 위도와 경도 값을 추출
                JsonNode locationNode = root.get(0);
                double lat = locationNode.path("lat").asDouble();
                double lon = locationNode.path("lon").asDouble();
                return new double[]{lat, lon}; // 위도와 경도를 double 배열로 반환
            } else {
                // 검색 결과가 없는 경우 예외를 발생
                throw new LocationNotFoundException("주소를 찾을 수 없습니다: " + address);
            }
        } catch (WebClientResponseException e) {
            // HTTP 요청 오류 시 예외 처리
            throw new RuntimeException("HTTP 요청 오류: " + e.getMessage(), e);
        } catch (Exception e) {
            // 일반적인 예외 처리
            throw new RuntimeException("위도 경도를 얻는데 실패하였습니다.", e);
        }
    }

    /**
     * 사용자 정의 예외 클래스: 주소를 찾을 수 없는 경우에 사용
     */
    private static class LocationNotFoundException extends RuntimeException {
        public LocationNotFoundException(String message) {
            super(message);
        }
    }
}
