package luckyvicky.petharmony.entity.board;

import lombok.Getter;

@Getter
public enum ReportProcess {
    THREE_DAY_SUSPENSION("3일정지"),
    SEVEN_DAY_SUSPENSION("7일정지"),
    DELETE_POST("삭제처리"),
    ACCOUNT_TERMINATION("탈퇴처리"),
    IGNORE_REPORT("신고무시하기"),
    PENDING("신고보류"),
    UNPROCESSED("미처리");


    private final String description;

    ReportProcess(String description) {
        this.description = description;
    }

}
