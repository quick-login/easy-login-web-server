package kr.co.easylogin.easyloginwebserver.question.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionStatus {
    WAITING("답변 대기"),
    COMPLETED("답변 완료");

    private final String description;
}
