package luckyvicky.petharmony.controller;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.repository.PetInfoRepository;
import luckyvicky.petharmony.security.JwtTokenProvider;
import luckyvicky.petharmony.service.PetInfoFormatService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetInfoController.class) // PetInfoController만 로드하여 테스트
@AutoConfigureMockMvc(addFilters = false) // 모든 시큐리티 필터 비활성화
public class PetInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetInfoFormatService petInfoFormatService;

    @MockBean
    private PetInfoRepository petInfoRepository;

    @MockBean
    private UserDetailsService userDetailsService; // UserDetailsService를 모킹하여 의존성 해결

    @MockBean
    private JwtTokenProvider jwtTokenProvider; // JwtTokenProvider를 모킹하여 의존성 해결

    /**
     * 모든 유기동물 정보를 반환하는 엔드포인트를 테스트하는 메서드
     */
    @Test
    void testGetAllPetsInfo() throws Exception {
        // Given: 테스트에 사용할 PetInfo 리스트를 설정
        List<PetInfo> mockPetInfos = Arrays.asList(
                PetInfo.builder()
                        .desertionNo("123456789")
                        .kindCd("[개] 포메라니안")
                        .age("2019(년생)")
                        .sexCd("M")
                        .neuterYn("Y")
                        .words("1,2,3")
                        .careNm("서울동물보호센터")
                        .build()
        );

        // PetInfoRepository의 findAll 메서드 호출 시, mockPetInfos 리스트 반환하도록 설정
        when(petInfoRepository.findAll()).thenReturn(mockPetInfos);

        // PetInfoFormatService의 processPetInfo 메서드 호출 시, 결과 맵 반환하도록 설정
        Map<String, Object> processedResult = new HashMap<>();
        processedResult.put("words", Arrays.asList("건강한", "회복중인", "온순한"));
        processedResult.put("kind_cd", "포메라니안");
        processedResult.put("age", "2019년생");
        processedResult.put("sex_cd", "남아");
        processedResult.put("neuter_yn", "중성화 완료");
        processedResult.put("care_nm", "서울동물보호센터");
        when(petInfoFormatService.processPetInfo(Mockito.any(PetInfo.class))).thenReturn(processedResult);

        // When: 매핑된 엔드포인트를 MockMvc를 통해 호출
        mockMvc.perform(MockMvcRequestBuilders.get("/api/public/allPetsInfo"))
                .andExpect(status().isOk())  // HTTP 상태 코드가 200 OK인지 확인
                .andExpect(jsonPath("$[0].words").isArray())  // 반환된 JSON 배열에서 'words' 필드가 배열인지 확인
                .andExpect(jsonPath("$[0].words[0]").value("건강한"))  // 'words' 배열의 첫 번째 요소가 "건강한"인지 확인
                .andExpect(jsonPath("$[0].kind_cd").value("포메라니안"))  // 'kind_cd' 필드의 값이 "포메라니안"인지 확인
                .andExpect(jsonPath("$[0].age").value("2019년생"))  // 'age' 필드의 값이 "2019년생"인지 확인
                .andExpect(jsonPath("$[0].sex_cd").value("남아"))  // 'sex_cd' 필드의 값이 "남아"인지 확인
                .andExpect(jsonPath("$[0].neuter_yn").value("중성화 완료"))  // 'neuter_yn' 필드의 값이 "중성화 완료"인지 확인
                .andExpect(jsonPath("$[0].care_nm").value("서울동물보호센터"));  // 'care_nm' 필드의 값이 "서울동물보호센터"인지 확인
    }
}
