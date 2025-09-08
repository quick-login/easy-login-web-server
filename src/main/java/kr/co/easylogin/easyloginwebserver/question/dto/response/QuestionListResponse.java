package kr.co.easylogin.easyloginwebserver.question.dto.response;

import java.time.LocalDateTime;
import kr.co.easylogin.easyloginwebserver.question.domain.Question;
import kr.co.easylogin.easyloginwebserver.question.value.QuestionStatus;
import lombok.Builder;

@Builder
public record QuestionListResponse(
    Long questionId,
    String title,
    String content,
    QuestionStatus status,
    LocalDateTime questionDate
) {

    public static QuestionListResponse of(Question question) {
        return QuestionListResponse.builder()
                                   .questionId(question.getId())
                                   .title(question.getTitle())
                                   .content(question.getContent())
                                   .status(question.getStatus())
                                   .questionDate(question.getCreatedAt())
                                   .build();
    }
}
