package luckyvicky.petharmony.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
public class LocationServiceTest {

    private LocationService locationService;
    private MockRestServiceServer mockServer;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        // RestTemplate 객체를 생성하고 MockRestServiceServer로 모의 서버를 설정
        this.restTemplate = new RestTemplate();
        this.locationService = new LocationService(restTemplate);
        this.mockServer = MockRestServiceServer.createServer(restTemplate);
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetLatLonFromAddress_ValidAddress() throws Exception {
        // given: 유효한 주소와 그에 해당하는 위도와 경도 응답을 설정
        String address = "서울특별시";
        double expectedLat = 37.5665;
        double expectedLon = 126.9780;
        String responseJson = "[{\"lat\":\"37.5665\",\"lon\":\"126.9780\"}]";
        URI uri = UriComponentsBuilder.fromHttpUrl("https://nominatim.openstreetmap.org/search")
                .queryParam("q", address)
                .queryParam("format", "json")
                .build().toUri();

        mockServer.expect(once(), requestTo(uri))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        // when: LocationService의 getLatLonFromAddress 메서드를 호출
        double[] actualLatLon = locationService.getLatLonFromAddress(address);

        // then: 반환된 위도와 경도가 예상 값과 동일한지 확인
        assertArrayEquals(new double[]{expectedLat, expectedLon}, actualLatLon, 0.0001);

        // 모의 서버가 기대한 대로 호출되었는지 확인
        mockServer.verify();
    }

    @Test
    public void testGetLatLonFromAddress_InvalidAddress() {
        // given: 유효하지 않은 주소와 빈 응답을 설정
        String address = "유효하지 않은 주소";
        String responseJson = "[]";
        URI uri = UriComponentsBuilder.fromHttpUrl("https://nominatim.openstreetmap.org/search")
                .queryParam("q", address)
                .queryParam("format", "json")
                .build().toUri();

        mockServer.expect(once(), requestTo(uri))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        // when & then: LocationService의 getLatLonFromAddress 메서드를 호출할 때 예외가 발생하는지 확인
        assertThrows(RuntimeException.class, () -> locationService.getLatLonFromAddress(address));

        // 모의 서버가 기대한 대로 호출되었는지 확인
        mockServer.verify();
    }
}
