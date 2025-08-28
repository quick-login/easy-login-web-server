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
    EMAIL_VERIFIED_EXPIRED(HttpStatus.BAD_REQUEST, "U1006", "이메일 인증 시간이 만료되었습니다."),
    EMAIL_UNVERIFIED_ERROR(HttpStatus.BAD_REQUEST, "U1007", "인증되지 않은 이메일입니다."),
    INVALID_LOGIN_INFO(HttpStatus.UNAUTHORIZED, "U1008", "로그인 인증정보가 일치하지 않습니다."),

    // 카카오 앱 에러
    APP_REGISTRATION_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "K2000", "등록 가능한 앱 가능 수를 초과하였습니다."),
    IS_PRESENT_KAKAO_APP(HttpStatus.BAD_REQUEST, "K2001", "이지로그인에 이미 등록되어있는 앱입니다. 관리자에게 문의해주세요"),
    KAKAO_APP_NOT_FOUND(HttpStatus.NOT_FOUND, "K2002", "등록된 앱을 찾을 수 없습니다."),
    KAKAO_APP_FORBIDDEN(HttpStatus.FORBIDDEN, "K2003", "해당 앱에 접근할 수 있는 권한이 없습니다."),

    // 사용자 입력 에러
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "E4000", "유효하지 않은 입력값입니다."),
    MINIMUM_AMOUNT_ERROR(HttpStatus.BAD_REQUEST, "E4001", "최소 충전금액 미달입니다."),
    AMOUNT_NOT_MULTIPLE_OF_100(HttpStatus.BAD_REQUEST, "E4002", "충전금액은 100원 단위로 입력해야 합니다."),
    API_FORBIDDEN(HttpStatus.FORBIDDEN, "E4003", "조회 및 호출 권한이 없습니다."),
    CASH_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "E4004", "캐시 충전 로그를 찾을 수 없습니다."),
    CHANGE_STATUS_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "E4005", "변경 가능한 상태가 아닙니다."),
    INVALID_PAGE_ERROR(HttpStatus.BAD_REQUEST, "E4006", "유효하지 않은 페이지 정보"),
    INVALID_DISCOUNT_RATE(HttpStatus.BAD_REQUEST, "E4007", "할인율은 0에서 100 사이의 값이어야 합니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "E4008", "상품을 찾을 수 없습니다."),
    DELETED_PRODUCT(HttpStatus.BAD_REQUEST, "E4009", "삭제된 상품입니다."),

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
