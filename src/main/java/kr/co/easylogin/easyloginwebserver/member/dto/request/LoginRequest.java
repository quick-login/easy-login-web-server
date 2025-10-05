package kr.co.easylogin.easyloginwebserver.member.dto.request;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Getter;

@Getter
@JsonFilter("sensitiveFilter")
public class LoginRequest {

    private String email;

    private String password;
}
