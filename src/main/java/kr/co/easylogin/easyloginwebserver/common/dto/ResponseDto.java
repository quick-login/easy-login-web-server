package kr.co.easylogin.easyloginwebserver.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonPropertyOrder({"code", "message", "data"})
public class ResponseDto<T> {

    @JsonIgnore
    private final HttpStatus status;
    private final String code;
    private final String message;
    private final T data;

    public ResponseDto(ResponseCode code, T data) {
        this.status = code.getStatus();
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
