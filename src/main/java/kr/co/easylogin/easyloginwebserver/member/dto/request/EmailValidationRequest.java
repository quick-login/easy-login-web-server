package kr.co.easylogin.easyloginwebserver.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class EmailValidationRequest {

    @NotBlank(message = "이메일은 비어있을 수 없습니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    @Size(max = 50, message = "이메일의 최대 길이는 50글자 이하여야 합니다.")
    private String email;

    @NotBlank(message = "인증코드는 비어있을 수 없습니다.")
    private String code;
}
