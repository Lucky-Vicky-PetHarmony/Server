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
        // Given: 특이사항이 포함된 문자열
        String specialMark = "코검정. 온순/친화적. 꼬리단미안됨. 털상태양호. 진드기오염.";

        // When: analyzeSpecialMark 메서드를 호출하여 결과를 얻음
        String response = openAiService.analyzeSpecialMark(specialMark);

        // OpenAI 응답 출력
        System.out.println("OpenAI 응답: " + response);

        // Then: 응답이 null이 아니고, 특정 키워드가 포함되어 있는지 확인
        assertThat(response).isNotNull();

        // 다음 키워드가 응답에 포함되어 있는지 확인
        boolean containsKeyword = false;
        String[] keywords = {"건강한", "회복중인", "온순한", "사나운", "활발한", "차분한",
                "겁많은", "호기심많은", "사교적인", "내성적인", "예쁜", "귀여운",
                "멋진", "평범한", "순종적인", "독립적인", "특별한", "독특한",
                "일반적인", "윤기나는"};


        // 키워드를 포함하는지 확인
        for (String keyword : keywords) {
            if (response.contains(keyword)) {
                containsKeyword = true;
                System.out.println("매칭된 키워드: " + keyword);
                break;  // 하나의 키워드를 찾으면 루프 종료
            }
        }

        // 응답에서 예상 키워드를 찾지 못한 경우: 응답을 출력하여 확인
        if (!containsKeyword) {
            System.out.println("예상한 키워드가 응답에 포함되지 않았습니다. 응답: " + response);
        }

        // 적어도 하나의 키워드가 응답에 포함되었는지 확인
        assertThat(containsKeyword).isTrue();
    }
}
