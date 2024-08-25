package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.ShelterInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ShelterInfoServiceTest {

    private ShelterInfoService shelterInfoService;

    @BeforeEach
    public void setUp() {
        shelterInfoService = new ShelterInfoService();
    }

    @Test
    public void testProcessShelterInfo() {
        // Given
        ShelterInfo shelterInfo = ShelterInfo.builder()
                .careNm("서울보호소")
                .careAddr("서울특별시 어딘가")
                .saveTrgtAnimal("개+고양이+기타")
                .weekOprStime("09:00")
                .weekOprEtime("18:00")
                .weekendOprStime("10:00")
                .weekendOprEtime("14:00")
                .closeDay("일요일")
                .careTel("010-1234-5678")
                .build();

        // When
        Map<String, Object> result = shelterInfoService.processShelterInfo(shelterInfo);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get("care_nm")).isEqualTo("서울보호소");
        assertThat(result.get("care_addr")).isEqualTo("서울특별시 어딘가");
        assertThat(result.get("save_trgt_animal")).isEqualTo("개,고양이,기타");
        assertThat(result.get("week_operating_hours")).isEqualTo("09:00 ~ 18:00");
        assertThat(result.get("weekend_operating_hours")).isEqualTo("10:00 ~ 14:00");
        assertThat(result.get("close_day")).isEqualTo("일요일");
        assertThat(result.get("care_tel")).isEqualTo("010-1234-5678");
    }

    @Test
    public void testProcessShelterInfoWithNullValues() {
        // Given
        ShelterInfo shelterInfo = ShelterInfo.builder()
                .careNm("서울보호소")
                .careAddr("서울특별시 어딘가")
                .saveTrgtAnimal(null)
                .weekOprStime(null)
                .weekOprEtime(null)
                .weekendOprStime(null)
                .weekendOprEtime(null)
                .closeDay(null)
                .careTel(null)
                .build();

        // When
        Map<String, Object> result = shelterInfoService.processShelterInfo(shelterInfo);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get("care_nm")).isEqualTo("서울보호소");
        assertThat(result.get("care_addr")).isEqualTo("서울특별시 어딘가");
        assertThat(result.get("save_trgt_animal")).isEqualTo("정보 없음");
        assertThat(result.get("week_operating_hours")).isEqualTo("운영 시간 없음");
        assertThat(result.get("weekend_operating_hours")).isEqualTo("운영 안함");
        assertThat(result.get("close_day")).isNull();
        assertThat(result.get("care_tel")).isNull();
    }

    @Test
    public void testProcessAnimal() {
        // Given
        ShelterInfo shelterInfo = ShelterInfo.builder()
                .saveTrgtAnimal("개+고양이+기타")
                .build();

        // When
        String processedAnimal = shelterInfoService.processShelterInfo(shelterInfo).get("save_trgt_animal").toString();

        // Then
        assertThat(processedAnimal).isEqualTo("개,고양이,기타");
    }

    @Test
    public void testProcessWeekHours() {
        // Given
        ShelterInfo shelterInfo = ShelterInfo.builder()
                .weekOprStime("09:00")
                .weekOprEtime("18:00")
                .build();

        // When
        String weekHours = shelterInfoService.processShelterInfo(shelterInfo).get("week_operating_hours").toString();

        // Then
        assertThat(weekHours).isEqualTo("09:00 ~ 18:00");
    }

    @Test
    public void testProcessWeekendHours() {
        // Given
        ShelterInfo shelterInfo = ShelterInfo.builder()
                .weekendOprStime("10:00")
                .weekendOprEtime("14:00")
                .build();

        // When
        String weekendHours = shelterInfoService.processShelterInfo(shelterInfo).get("weekend_operating_hours").toString();

        // Then
        assertThat(weekendHours).isEqualTo("10:00 ~ 14:00");
    }
}
