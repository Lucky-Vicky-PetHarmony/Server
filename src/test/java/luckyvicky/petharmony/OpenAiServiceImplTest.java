package luckyvicky.petharmony;

import luckyvicky.petharmony.service.openapi.OpenAiServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OpenAiServiceImplTest {

    @Autowired
    private OpenAiServiceImpl openAiService;

    @Test
    public void testAnalyzeSpecialMark() {
        String specialMark = "A unique pattern";
        String response = openAiService.analyzeSpecialMark(specialMark);

        // 응답이 null이 아니고, 특정한 내용을 포함하는지 확인합니다.
        assertThat(response).isNotNull();
        assertThat(response).contains("unique"); // 예시로 unique라는 단어가 포함되는지 확인합니다.
    }
}
