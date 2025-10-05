package kr.co.easylogin.easyloginwebserver.member.dto.request;

import com.fasterxml.jackson.annotation.JsonFilter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@JsonFilter("sensitiveFilter")
public class SignupRequest {

    @NotBlank(message = "이메일은 비어있을 수 없습니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    @Size(max = 50, message = "이메일의 최대 길이는 50글자 이하여야 합니다.")
    private String email;

    @NotBlank(message = "이름은 비어있을 수 없습니다.")
    @Size(max = 10, message = "이름은 최대 10글자입니다.")
    private String name;

    @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
    @Size(min = 6, max = 30, message = "비밀번호는 6자 이상, 30자 이하여야 합니다.")
    @Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*~])[a-zA-Z0-9!@#$%^&*~]+$",
        message = "비밀번호는 알파벳, 숫자, 특수문자를 모두 포함해야 합니다."
    )
    private String password;

    @NotBlank(message = "비밀번호 확인은 비어있을 수 없습니다.")
    @Size(min = 6, max = 30, message = "비밀번호 확인은 6자 이상, 30자 이하여야 합니다.")
    private String passwordCheck;

    private String kakaoId;
}
