package luckyvicky.petharmony.entity.board;

import lombok.Getter;

@Getter
public enum ReportType {
    PROFANITY_ABUSE("욕설 및 비방"),
    FALSE_INFORMATION("허위 정보"),
    SPAM_ADVERTISEMENT("스팸 및 광고"),
    PRIVACY_VIOLATION("개인정보 노출"),
    REPEATED_POSTING("도배 및 반복 게시"),
    OBSCENE_ILLEGAL_CONTENT("음란물 및 불법 콘텐츠"),
    COPYRIGHT_INFRINGEMENT("저작권 침해"),
    OFF_TOPIC("주제와 무관한 글"),
    IMPERSONATION_IDENTITY_THEFT("사칭 및 명의 도용"),
    VIOLENT_HATEFUL_CONTENT("폭력적이거나 혐오스러운 콘텐츠"),
    OTHER("기타");

    private final String description;

    ReportType(String description) {
        this.description = description;
    }

}
