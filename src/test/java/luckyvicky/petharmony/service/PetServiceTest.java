package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.ShelterInfo;
import luckyvicky.petharmony.repository.PetInfoRepository;
import luckyvicky.petharmony.repository.ShelterInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PetServiceTest {

    @Mock
    private LocationService locationService;

    @Mock
    private ShelterInfoRepository shelterInfoRepository;

    @Mock
    private PetInfoRepository petInfoRepository;

    @InjectMocks
    private PetService petService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // 모킹을 초기화
    }

    @Test
    void getPetsNearUser_WithValidAddressAndNearbyShelters_ReturnsPetInfoList() {
        // Given: Mock 데이터 설정
        String address = "12345";
        double[] userLatLng = {37.5665, 126.9780}; // 서울의 위도 경도 예시

        // 사용자의 위치와 가까운 보호소 정보 모킹
        ShelterInfo shelterInfo = new ShelterInfo();
        shelterInfo.setCareNm("Care Center 1");
        List<ShelterInfo> nearbyShelters = Collections.singletonList(shelterInfo);

        // 보호소의 반려동물 정보 모킹
        PetInfo petInfo = new PetInfo();
        petInfo.setDesertionNo("1");
        List<PetInfo> petInfos = Collections.singletonList(petInfo);

        // When: 모킹 설정
        when(locationService.getLatLonFromAddress(address)).thenReturn(userLatLng);
        when(shelterInfoRepository.findWithinDistance(userLatLng[0], userLatLng[1], 30.0)).thenReturn(nearbyShelters);
        when(petInfoRepository.findAllByCareNm("Care Center 1")).thenReturn(petInfos);

        // When: 테스트 실행
        List<PetInfo> result = petService.getPetsNearUser(address);

        // Then: 검증
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDesertionNo()).isEqualTo("1");

        verify(locationService, times(1)).getLatLonFromAddress(address);
        verify(shelterInfoRepository, times(1)).findWithinDistance(userLatLng[0], userLatLng[1], 30.0);
        verify(petInfoRepository, times(1)).findAllByCareNm("Care Center 1");
    }

    @Test
    void getPetsNearUser_WithValidAddressAndNoNearbyShelters_ReturnsNearestPets() {
        // Given: Mock 데이터 설정
        String address = "12345";
        double[] userLatLng = {37.5665, 126.9780}; // 서울의 위도 경도 예시

        // 사용자의 위치와 가까운 보호소가 없고, 가장 가까운 30개의 보호소 정보를 모킹
        List<ShelterInfo> nearestShelters = Arrays.asList(new ShelterInfo(), new ShelterInfo());
        nearestShelters.get(0).setCareNm("Care Center 2");
        nearestShelters.get(1).setCareNm("Care Center 3");

        // 보호소의 반려동물 정보 모킹
        PetInfo petInfo1 = new PetInfo();
        petInfo1.setDesertionNo("2");
        PetInfo petInfo2 = new PetInfo();
        petInfo2.setDesertionNo("3");

        List<PetInfo> petInfos1 = Collections.singletonList(petInfo1);
        List<PetInfo> petInfos2 = Collections.singletonList(petInfo2);

        // When: 모킹 설정
        when(locationService.getLatLonFromAddress(address)).thenReturn(userLatLng);
        when(shelterInfoRepository.findWithinDistance(userLatLng[0], userLatLng[1], 30.0)).thenReturn(Collections.emptyList());
        when(shelterInfoRepository.findNearestShelters(userLatLng[0], userLatLng[1], PageRequest.of(0, 30))).thenReturn(nearestShelters);
        when(petInfoRepository.findAllByCareNm("Care Center 2")).thenReturn(petInfos1);
        when(petInfoRepository.findAllByCareNm("Care Center 3")).thenReturn(petInfos2);

        // When: 테스트 실행
        List<PetInfo> result = petService.getPetsNearUser(address);

        // Then: 검증
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getDesertionNo()).isEqualTo("2");
        assertThat(result.get(1).getDesertionNo()).isEqualTo("3");

        verify(locationService, times(1)).getLatLonFromAddress(address);
        verify(shelterInfoRepository, times(1)).findWithinDistance(userLatLng[0], userLatLng[1], 30.0);
        verify(shelterInfoRepository, times(1)).findNearestShelters(userLatLng[0], userLatLng[1], PageRequest.of(0, 30));
        verify(petInfoRepository, times(1)).findAllByCareNm("Care Center 2");
        verify(petInfoRepository, times(1)).findAllByCareNm("Care Center 3");
    }

    @Test
    void getPetsNearUser_WithInvalidAddress_ThrowsIllegalArgumentException() {
        // Given: Invalid address
        String invalidAddress = "";

        // When & Then: 예외가 발생하는지 검증
        assertThrows(IllegalArgumentException.class, () -> petService.getPetsNearUser(invalidAddress));

        verify(locationService, never()).getLatLonFromAddress(anyString());
        verify(shelterInfoRepository, never()).findWithinDistance(anyDouble(), anyDouble(), anyDouble());
        verify(petInfoRepository, never()).findAllByCareNm(anyString());
    }
}
