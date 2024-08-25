package luckyvicky.petharmony.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import luckyvicky.petharmony.entity.ShelterInfo;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.repository.ShelterInfoRepository;
import luckyvicky.petharmony.repository.UserRepository;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class LocationService {

    // WebClient 인스턴스, 사용자 리포지토리, 쉘터 정보 리포지토리를 의존성 주입 받음
    private final WebClient webClient;
    private final UserRepository userRepository;
    private final ShelterInfoRepository shelterInfoRepository;

    // 생성자를 통해 의존성 주입을 받음
    @Autowired
    public LocationService(WebClient.Builder webClientBuilder, UserRepository userRepository, ShelterInfoRepository shelterInfoRepository) {
        this.webClient = webClientBuilder.baseUrl("https://nominatim.openstreetmap.org")
                .defaultHeader(HttpHeaders.USER_AGENT, "YourAppName") // 기본 User-Agent 헤더 설정
                .build();
        this.userRepository = userRepository;
        this.shelterInfoRepository = shelterInfoRepository;
    }

    // 주소를 기반으로 위도와 경도를 가져오는 메서드
    public double[] getLatLonFromAddress(String address) {
        // 주소가 null이거나 비어있으면 예외 발생
        if (address == null || address.trim().isEmpty()) {
            throw new AddressNotFoundException("주소 정보가 없습니다.");
        }

        try {
            // OpenStreetMap Nominatim API를 이용해 주소를 기반으로 위도와 경도를 검색
            URI uri = UriComponentsBuilder.fromUriString("https://nominatim.openstreetmap.org/search")
                    .queryParam("q", address) // 검색 쿼리 파라미터 추가
                    .queryParam("format", "json") // 결과 형식 지정
                    .encode()
                    .build().toUri();

            // WebClient를 사용하여 HTTP GET 요청을 보내고 응답을 문자열로 수신
            String response = this.webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // JSON 응답을 파싱하여 위도와 경도 값을 추출
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);

            // JSON 배열이 비어있지 않으면 첫 번째 결과의 위도와 경도 값을 반환
            if (root.isArray() && root.size() > 0) {
                JsonNode locationNode = root.get(0);
                double lat = locationNode.path("lat").asDouble();
                double lon = locationNode.path("lon").asDouble();

                // 위도와 경도의 유효성 검사
                if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
                    lat = 35.00; // 기본 위도 설정
                    lon = 125.00; // 기본 경도 설정
                }
                return new double[]{lat, lon};
            } else {
                // 검색 결과가 없으면 예외 발생
                throw new LocationNotFoundException("주소를 찾을 수 없습니다: " + address);
            }
        } catch (WebClientResponseException e) {
            // HTTP 요청 실패 시 예외 발생
            throw new RuntimeException("HTTP 요청 오류: " + e.getMessage(), e);
        } catch (Exception e) {
            // 기타 예외 발생 시 처리
            throw new RuntimeException("위도 경도를 얻는데 실패하였습니다.", e);
        }
    }

    // 쉘터 정보에서 위도와 경도를 가져오는 메서드
    public double[] getLatLonFromShelterInfo(ShelterInfo shelterInfo) {
        // 쉘터 정보의 위도와 경도가 null인 경우 기본값으로 설정
        double lat = shelterInfo.getLat() != null ? shelterInfo.getLat().doubleValue() : 35.00;
        double lon = shelterInfo.getLng() != null ? shelterInfo.getLng().doubleValue() : 125.00;

        // 위도와 경도의 유효성 검사
        if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
            lat = 35.00; // 기본 위도 설정
            lon = 125.00; // 기본 경도 설정
        }

        return new double[]{lat, lon};
    }

    // 사용자 ID를 기반으로 사용자의 위도와 경도를 가져오는 메서드
    public double[] getLatLonFromUserId(Long userId) {
        // 사용자 ID로 사용자 정보를 찾음
        Optional<User> userOptional = userRepository.findById(userId);
        // 사용자가 존재하지 않으면 예외 발생
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("사용자 ID에 해당하는 사용자가 없습니다: " + userId);
        }

        User user = userOptional.get();
        String address = user.getAddress();

        // 사용자의 주소가 없으면 예외 발생
        if (address == null || address.trim().isEmpty()) {
            throw new AddressNotFoundException("사용자 ID " + userId + "의 주소 정보가 없습니다.");
        }

        // 주소를 기반으로 위도와 경도를 반환
        return getLatLonFromAddress(address);
    }

    /**
     * 두 지점 간의 거리를 계산하는 메서드 (단위: km)
     * @param lat1 첫 번째 지점의 위도
     * @param lon1 첫 번째 지점의 경도
     * @param lat2 두 번째 지점의 위도
     * @param lon2 두 번째 지점의 경도
     * @return 두 지점 간의 거리 (km)
     */
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371; // 지구 반지름 (km 단위)
        double latDistance = Math.toRadians(lat2 - lat1); // 위도 차이
        double lonDistance = Math.toRadians(lon2 - lon1); // 경도 차이
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)); // 중심각 계산
        return EARTH_RADIUS_KM * c; // 두 지점 간의 거리 반환
    }

    // 주소를 찾을 수 없을 때 던지는 예외 클래스
    private static class LocationNotFoundException extends RuntimeException {
        public LocationNotFoundException(String message) {
            super(message);
        }
    }

    // 사용자를 찾을 수 없을 때 던지는 예외 클래스
    private static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    // 주소 정보가 없을 때 던지는 예외 클래스
    private static class AddressNotFoundException extends RuntimeException {
        public AddressNotFoundException(String message) {
            super(message);
        }
    }
}
