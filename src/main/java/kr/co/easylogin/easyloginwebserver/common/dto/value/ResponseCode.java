package kr.co.easylogin.easyloginwebserver.common.dto.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {

    // 성공 응답
    SUCCESS(HttpStatus.OK, "E200", "Success"),

    // 사용자 에러
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "U1000", "현재 가입되어있는 이메일입니다."),
    PASSWORD_CHECK_ERROR(HttpStatus.BAD_REQUEST, "U1001", "비밀번호와 비밀번호 확인값이 일치하지 않습니다."),

    // 사용자 입력 에러
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "E4000", "유효하지 않은 입력값입니다."),

    // 서버 에러
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E5000", "정의되지않은 서버 에러");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
