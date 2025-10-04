package kr.co.easylogin.easyloginwebserver.common.response;

import org.springframework.validation.FieldError;

public record InvalidInputResponse(
    String field,
    String message
) {

    public static InvalidInputResponse of(FieldError fieldError) {
        return new InvalidInputResponse(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
