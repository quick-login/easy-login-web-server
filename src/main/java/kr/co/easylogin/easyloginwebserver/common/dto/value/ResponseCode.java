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
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U1002", "회원을 찾을 수 없습니다."),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "U1003", "로그인이 필요합니다."),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "U1004", "로그인 과정에서 오류 발생"),
    EMAIL_CODE_INVALID(HttpStatus.BAD_REQUEST, "U1005", "올바르지 않은 이메일 인증 코드입니다."),

    // 사용자 입력 에러
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "E4000", "유효하지 않은 입력값입니다."),

    // 서버 에러
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E5000", "정의되지않은 서버 에러"),
    MAIL_CODE_CREATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E5001", "메일 인증코드 생성중 오류가 발생했습니다."),
    MAIL_EXPIRED_3_MIN(HttpStatus.INTERNAL_SERVER_ERROR, "E5002", "메일 인증코드가 만료되었습니다. 3분내로 입력해주세요."),

    // JWT 관련 에러
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "T6000", "유효하지 않은 토큰입니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "T6001", "만료된 Access Token 입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "T6002", "만료된 Refresh Token 입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
