package kr.co.easylogin.easyloginwebserver.question.dto.response;

import java.time.format.DateTimeFormatter;
import kr.co.easylogin.easyloginwebserver.question.domain.Question;
import kr.co.easylogin.easyloginwebserver.question.value.QuestionStatus;
import lombok.Builder;

@Builder
public record AdminQuestionListResponse(
    Long questionId,
    String name,
    String title,
    QuestionStatus status,
    String questionDate
) {

    public static AdminQuestionListResponse of(Question question) {
        return AdminQuestionListResponse.builder()
                                        .questionId(question.getId())
                                        .name(question.getMember().getName())
                                        .title(question.getTitle())
                                        .status(question.getStatus())
                                        .questionDate(question.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                        .build();
    }
}
