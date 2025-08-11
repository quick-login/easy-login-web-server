package kr.co.easylogin.easyloginwebserver.common.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"code", "message", "data"})
public class ResponseDto<T> {

    private final String code;
    private final String message;
    private final T data;

    public ResponseDto(ResponseCode code, T data) {
        this.code = code.getCode();
        this.message = code.getMessage();
        this.data = data;
    }

    public static <T> ResponseDto<T> of(ResponseCode code, T data) {
        return new ResponseDto<>(code, data);
    }

    public static <T> ResponseDto<T> of(ResponseCode code) {
        return new ResponseDto<>(code, null);
    }
}
