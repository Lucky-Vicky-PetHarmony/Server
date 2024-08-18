package luckyvicky.petharmony;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.repository.PetInfoRepository;
import luckyvicky.petharmony.service.PetInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

public class PetInfoRepositoryTest {

    @Mock
    private PetInfoRepository petInfoRepository; // Mock 객체 생성

    @InjectMocks
    private PetInfoService petInfoService; // 실제 서비스를 테스트할 때 사용

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mock 객체 초기화
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUserName("Test User");
        testUser.setEmail("testuser@example.com");
    }

    @Test
    public void testFindTop12ByUserWord() {
        // Mock 데이터를 준비
        List<PetInfo> mockPetInfos = new ArrayList<>();
        PetInfo petInfo1 = new PetInfo();
        petInfo1.setWords("온순한, 윤기나는");
        mockPetInfos.add(petInfo1);

        PetInfo petInfo2 = new PetInfo();
        petInfo2.setWords("활발한, 사교적인");
        mockPetInfos.add(petInfo2);

        // Mocking: petInfoRepository.findTop12ByUserWord 호출 시 mockPetInfos 반환
        when(petInfoRepository.findTop12ByUserWord(any(User.class), any(PageRequest.class)))
                .thenReturn(mockPetInfos);

        // 실제 테스트 수행
        List<PetInfo> petInfos = petInfoService.getTop12PetInfosByUserWord(testUser);

        // 검증: 반환된 결과가 예상과 일치하는지 확인
        assertThat(petInfos).isNotNull();
        assertThat(petInfos.size()).isEqualTo(2);

        // 반환된 PetInfo의 단어를 확인
        assertThat(petInfos.get(0).getWords()).contains("온순한");
        assertThat(petInfos.get(1).getWords()).contains("사교적인");
    }
}
