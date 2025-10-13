package kr.co.easylogin.easyloginwebserver.question.dto.response;

import java.time.format.DateTimeFormatter;
import kr.co.easylogin.easyloginwebserver.question.domain.Question;
import kr.co.easylogin.easyloginwebserver.question.value.QuestionStatus;
import lombok.Builder;

@Builder
public record QuestionListResponse(
    Long questionId,
    String title,
    String content,
    QuestionStatus status,
    String questionDate
) {

    public static QuestionListResponse of(Question question) {
        return QuestionListResponse.builder()
                                   .questionId(question.getId())
                                   .title(question.getTitle())
                                   .content(question.getContent())
                                   .status(question.getStatus())
                                   .questionDate(question.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                   .build();
    }
}
