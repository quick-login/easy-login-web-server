package kr.co.easylogin.easyloginwebserver.common.error;

import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ResponseCode responseCode;

    public BusinessException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }
}
