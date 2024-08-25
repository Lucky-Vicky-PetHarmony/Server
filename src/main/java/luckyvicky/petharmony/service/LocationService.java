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

import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;

@Service
public class LocationService {

    private final WebClient webClient;
    private final UserRepository userRepository;
    private final ShelterInfoRepository shelterInfoRepository;

    @Autowired
    public LocationService(WebClient.Builder webClientBuilder, UserRepository userRepository, ShelterInfoRepository shelterInfoRepository) {
        this.webClient = webClientBuilder.baseUrl("https://nominatim.openstreetmap.org")
                .defaultHeader(HttpHeaders.USER_AGENT, "YourAppName")
                .build();
        this.userRepository = userRepository;
        this.shelterInfoRepository = shelterInfoRepository;
    }

    public double[] getLatLonFromAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new AddressNotFoundException("주소 정보가 없습니다.");
        }

        try {
            URI uri = UriComponentsBuilder.fromUriString("https://nominatim.openstreetmap.org/search")
                    .queryParam("q", address)
                    .queryParam("format", "json")
                    .encode()
                    .build().toUri();

            String response = this.webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);

            if (root.isArray() && root.size() > 0) {
                JsonNode locationNode = root.get(0);
                double lat = locationNode.path("lat").asDouble();
                double lon = locationNode.path("lon").asDouble();

                if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
                    lat = 35.00;
                    lon = 125.00;
                }
                return new double[]{lat, lon};
            } else {
                throw new LocationNotFoundException("주소를 찾을 수 없습니다: " + address);
            }
        } catch (WebClientResponseException e) {
            throw new RuntimeException("HTTP 요청 오류: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("위도 경도를 얻는데 실패하였습니다.", e);
        }
    }

    public double[] getLatLonFromShelterInfo(ShelterInfo shelterInfo) {
        double lat = shelterInfo.getLat() != null ? shelterInfo.getLat().doubleValue() : 35.00;
        double lon = shelterInfo.getLng() != null ? shelterInfo.getLng().doubleValue() : 125.00;

        if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
            lat = 35.00;
            lon = 125.00;
        }

        return new double[]{lat, lon};
    }

    public double[] getLatLonFromUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("사용자 ID에 해당하는 사용자가 없습니다: " + userId);
        }

        User user = userOptional.get();
        String address = user.getAddress();

        if (address == null || address.trim().isEmpty()) {
            throw new AddressNotFoundException("사용자 ID " + userId + "의 주소 정보가 없습니다.");
        }

        return getLatLonFromAddress(address);
    }

    /**
     * 두 지점 간의 거리를 계산하는 메서드 (단위: km)
     */
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    private static class LocationNotFoundException extends RuntimeException {
        public LocationNotFoundException(String message) {
            super(message);
        }
    }

    private static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    private static class AddressNotFoundException extends RuntimeException {
        public AddressNotFoundException(String message) {
            super(message);
        }
    }
}
