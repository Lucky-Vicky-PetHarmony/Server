package luckyvicky.petharmony;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.repository.PetInfoRepository;
import luckyvicky.petharmony.repository.UserWordRepository;
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
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

public class PetInfoServiceTest {

    @Mock
    private PetInfoRepository petInfoRepository;
    @Mock
    private UserWordRepository userWordRepository;
    @InjectMocks
    private PetInfoService petInfoService;

    private Long testUserId = 27L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mock객체 초기화
    }

    @Test
    public void testGetTop12PetInfosByUserWord() {

        List<Long> mockWordIds = List.of(1L, 3L, 5L, 20L);
        List<PetInfo> mockPetInfos = new ArrayList<>();
        PetInfo petInfo1 = new PetInfo();
        petInfo1.setDesertionNo("123456789");
        petInfo1.setWords("온순한, 윤기나는");
        mockPetInfos.add(petInfo1);

        PetInfo petInfo2 = new PetInfo();
        petInfo2.setDesertionNo("987654321");
        petInfo2.setWords("활발한, 사교적인");
        mockPetInfos.add(petInfo2);

        // Mocking: userWordRepository.findWordIdsByUserId 호출 시 mockWordIds반환
        when(userWordRepository.findWordIdsByUserId(any(Long.class)))
                .thenReturn(mockWordIds);

        // Mocking: petInfoRepository.findTop12ByWordIds 호출 시 mockPetInfos반화
        when(petInfoRepository.findTop12ByWordIds(any(List.class), any(PageRequest.class)))
                .thenReturn(mockPetInfos);

        // 실제 테스트 수행
        List<PetInfo> petInfos = petInfoService.getTop12PetInfosByUserWord(testUserId);

        // 검증: 반환된 결과가 예상과 일치하는지 확인
        assertThat(petInfos).isNotNull();
        assertThat(petInfos.size()).isLessThanOrEqualTo(12);

        // 반환된 PetInfo의 단어를 확인하고, desertion_no 출력
        for (PetInfo petInfo : petInfos) {
            System.out.println("Desertion No: " + petInfo.getDesertionNo() + ", Words: " + petInfo.getWords());
        }

        // 예상한 단어들이 포함되어 있는지 확인
        assertThat(petInfos.get(0).getWords()).contains("온순한");
        assertThat(petInfos.get(1).getWords()).contains("사교적인");
    }
}
