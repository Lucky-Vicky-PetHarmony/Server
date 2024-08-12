package luckyvicky.petharmony.service;

/**
 * OpenAiService는 OpenAI API와의 통신을 처리하는 서비스 인터페이스
 * 특정 텍스트 입력(특히, specialMark)을 분석하여 OpenAI API의 응답을 받아오는 역할
 *
 * 이 인터페이스는 구현 클래스에서 실제 API 호출 로직을 정의할 수 있도록 설계
 * 다양한 구현체를 통해 OpenAI API 또는 유사한 텍스트 분석 서비스와 상호작용
 */
public interface OpenAiService {
    String analyzeSpecialMark(String specialMark);
}
