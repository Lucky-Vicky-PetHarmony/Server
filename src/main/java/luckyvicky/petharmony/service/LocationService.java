package luckyvicky.petharmony.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * LocationService 클래스는 주소 정보를 통해 위도와 경도를 얻는 기능을 제공
 * Nominatim API를 사용하여 주소를 위도와 경도로 변환
 */
@Service
public class LocationService {
    private final RestTemplate restTemplate; // HTTP 요청을 보내기 위한 RestTemplate 객체

    /**
     * LocationService 생성자. RestTemplate 객체를 주입받아 초기
     *
     * @param restTemplate RestTemplate 객체
     */
    public LocationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
            URI uri = UriComponentsBuilder.fromHttpUrl("https://nominatim.openstreetmap.org/search")
                    .queryParam("q", address) // 검색 쿼리에 주소를 추가
                    .queryParam("format", "json") // 응답 포맷을 JSON으로 설정
                    .build().toUri();

            // API 요청을 보내고 JSON 형식의 응답을 문자열
            String response = restTemplate.getForObject(uri, String.class);

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
                throw new RuntimeException("주소를 찾을 수 없습니다.: " + address);
            }
        } catch (Exception e) {
            // API 요청이나 JSON 파싱에 실패한 경우 예외를 발생
            throw new RuntimeException("위도 경도를 얻는데 실패하였습니다.", e);
        }
    }
}
