package kr.co.easylogin.easyloginwebserver.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AnswerRequest {

    @NotBlank(message = "내용은 비어있을 수 없습니다.")
    @Size(max = 1000, message = "답변의 최대 길이는 1000글자 이하여야 합니다.")
    private String answer;
}
