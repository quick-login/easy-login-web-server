package kr.co.easylogin.easyloginwebserver.question.dto.response;

import java.time.LocalDateTime;
import kr.co.easylogin.easyloginwebserver.question.domain.Question;
import kr.co.easylogin.easyloginwebserver.question.value.QuestionStatus;
import lombok.Builder;

@Builder
public record QuestionInfoResponse(
    Long questionId,
    String title,
    String content,
    String answer,
    QuestionStatus status,
    LocalDateTime questionDate,
    LocalDateTime answeredDate
) {

    public static QuestionInfoResponse of(Question question) {
        return QuestionInfoResponse.builder()
                                   .questionId(question.getId())
                                   .title(question.getTitle())
                                   .content(question.getContent())
                                   .answer(question.getAnswer())
                                   .status(question.getStatus())
                                   .questionDate(question.getCreatedAt())
                                   .answeredDate(question.getAnsweredDate())
                                   .build();
    }
}
