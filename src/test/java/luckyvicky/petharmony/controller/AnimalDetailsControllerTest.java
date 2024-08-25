package luckyvicky.petharmony.controller;

import luckyvicky.petharmony.service.CombinedInfoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnimalDetailsController.class) // AnimalDetailsController만 로드하여 테스트
@AutoConfigureMockMvc(addFilters = false) // 모든 시큐리티 필터 비활성화
public class AnimalDetailsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CombinedInfoService combinedInfoService;  // CombinedInfoService를 모킹하여 의존성 해결

    @MockBean
    private UserDetailsService userDetailsService;  // UserDetailsService를 모킹하여 의존성 해결

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"}) // Mock된 사용자로 테스트 수행
    void testGetCombinedAnimalInfo() throws Exception {
        // Given: CombinedInfoService의 getCombinedInfo 메서드가 반환할 결과 설정
        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put("pet_info", "sample pet info");
        mockResult.put("shelter_info", "sample shelter info");

        when(combinedInfoService.getCombinedInfo(anyString())).thenReturn(mockResult);

        // When: /api/public/{desertionNo} 엔드포인트를 MockMvc를 통해 호출
        mockMvc.perform(MockMvcRequestBuilders.get("/api/public/123456789"))
                .andExpect(status().isOk())  // HTTP 상태 코드가 200 OK인지 확인
                .andExpect(jsonPath("$.pet_info").value("sample pet info"))  // 'pet_info' 필드의 값이 "sample pet info"인지 확인
                .andExpect(jsonPath("$.shelter_info").value("sample shelter info"));  // 'shelter_info' 필드의 값이 "sample shelter info"인지 확인
    }
}
