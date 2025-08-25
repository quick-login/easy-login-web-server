package kr.co.easylogin.easyloginwebserver.common.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"code", "message", "data", "pagination"})
public class PageResponseDto<T> extends ResponseDto<T> {

    private final PageDto pagination;

    protected PageResponseDto(ResponseCode code, T data, PageDto pageDto) {
        super(code, data);
        this.pagination = pageDto;
    }

    public static <T> PageResponseDto<T> of(ResponseCode code, T data, PageDto pageDto) {
        return new PageResponseDto<>(code, data, pageDto);
    }
}
