package kr.co.easylogin.easyloginwebserver.kakao.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ModifyKakaoAppRequest {

    @NotBlank(message = "카카오 앱 이름은 비어있을 수 없습니다.")
    @Size(max = 50, message = "앱 이름은 최대 50글자입니다.")
    private String appName;

    @NotBlank(message = "카카오 앱 키는 비어있을 수 없습니다.")
    @Size(max = 50, message = "앱 키는 최대 50글자입니다.")
    private String restKey;

    @Size(max = 255, message = "리다이렉트 url은 최대 255글자입니다.")
    private String redirectUrl;
}
