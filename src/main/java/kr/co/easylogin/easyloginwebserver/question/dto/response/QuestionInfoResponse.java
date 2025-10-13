package kr.co.easylogin.easyloginwebserver.question.dto.response;

import java.time.format.DateTimeFormatter;
import kr.co.easylogin.easyloginwebserver.question.domain.Question;
import kr.co.easylogin.easyloginwebserver.question.value.QuestionStatus;
import lombok.Builder;

@Builder
public record QuestionInfoResponse(
    Long questionId,
    String name,
    String title,
    String content,
    String answer,
    QuestionStatus status,
    String questionDate,
    String answeredDate
) {

    public static QuestionInfoResponse of(Question question) {
        return QuestionInfoResponse.builder()
                                   .questionId(question.getId())
                                   .name(question.getMember().getName())
                                   .title(question.getTitle())
                                   .content(question.getContent())
                                   .answer(question.getAnswer())
                                   .status(question.getStatus())
                                   .questionDate(question.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                   .answeredDate(question.getAnsweredDate() != null
                                                 ? question.getAnsweredDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                                 : null)
                                   .build();
    }
}
