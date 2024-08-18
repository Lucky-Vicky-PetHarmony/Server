package luckyvicky.petharmony;

import luckyvicky.petharmony.service.openapi.OpenAiServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OpenAiServiceImplTest {

    @Autowired
    private OpenAiServiceImpl openAiService;

    @Test
    public void testAnalyzeSpecialMark() {
        // Given: 특이사항이 포함된 문자열
        String specialMark = "경계. 예민. 사나움. 코검정. 우전지파행및부종. 털때탐. 꼬리단미안됨.";

        // When: analyzeSpecialMark 메서드를 호출하여 결과를 얻음
        String response = openAiService.analyzeSpecialMark(specialMark);

        // OpenAI 응답 출력
        System.out.println("OpenAI 응답: " + response);

        // Then: 응답이 null이 아니고, 특정 키워드가 포함되어 있는지 확인
        assertThat(response).isNotNull();

        // 다음 키워드가 응답에 포함되어 있는지 확인
        List<String> matchedKeywords = new ArrayList<>();
        String[] keywords = {"건강한", "회복중인", "온순한", "사나운", "활발한", "차분한",
                "겁많은", "호기심많은", "사교적인", "내성적인", "예쁜", "돌봄이 필요한",
                "멋진", "평범한", "순종적인", "독립적인", "특별한", "독특한",
                "일반적인", "윤기나는"};

        // 모든 키워드를 확인하고, 매칭된 키워드를 리스트에 추가
        for (String keyword : keywords) {
            if (response.contains(keyword)) {
                matchedKeywords.add(keyword);
            }
        }

        // 매칭된 키워드 출력
        if (!matchedKeywords.isEmpty()) {
            System.out.println("매칭된 키워드: " + String.join(", ", matchedKeywords));
        } else {
            System.out.println("예상한 키워드가 응답에 포함되지 않았습니다. 응답: " + response);
        }

        // 적어도 하나의 키워드가 응답에 포함되었는지 확인
        assertThat(matchedKeywords).isNotEmpty();
    }
}
