package kr.co.easylogin.easyloginwebserver.notice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class NoticeInitRequest {

    @NotBlank(message = "제목은 비어있을 수 없습니다.")
    @Size(max = 100, message = "제목의 최대 길이는 100글자 이하여야 합니다.")
    private String title;

    @NotBlank(message = "내용은 비어있을 수 없습니다.")
    @Size(max = 2000, message = "내용의 최대 길이는 2000글자 이하여야 합니다.")
    private String content;

    private Boolean fixed;
}
