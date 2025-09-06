package kr.co.easylogin.easyloginwebserver.question.dto.response;

import java.time.LocalDateTime;
import kr.co.easylogin.easyloginwebserver.question.domain.Question;
import kr.co.easylogin.easyloginwebserver.question.value.QuestionStatus;
import lombok.Builder;

@Builder
public record AdminQuestionListResponse(
    Long questionId,
    String name,
    String title,
    String content,
    QuestionStatus status,
    LocalDateTime date
) {

    public static AdminQuestionListResponse of(Question question) {
        return AdminQuestionListResponse.builder()
                                        .questionId(question.getId())
                                        .name(question.getMember().getName())
                                        .title(question.getTitle())
                                        .content(question.getContent())
                                        .status(question.getStatus())
                                        .date(question.getCreatedAt())
                                        .build();
    }
}
