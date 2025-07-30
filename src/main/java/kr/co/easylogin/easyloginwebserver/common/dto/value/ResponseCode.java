package kr.co.easylogin.easyloginwebserver.common.dto.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {

    // 성공 응답
    SUCCESS(HttpStatus.OK, "E200", "Success");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
