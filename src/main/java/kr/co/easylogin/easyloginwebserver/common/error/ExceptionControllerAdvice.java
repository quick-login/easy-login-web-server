package kr.co.easylogin.easyloginwebserver.common.error;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.easylogin.easyloginwebserver.common.dto.ResponseDto;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionControllerAdvice {

    private final HttpServletResponse response;

    @ExceptionHandler(value = BusinessException.class)
    public ResponseDto<Object> handleBusinessException(BusinessException exception) {
        response.setStatus(exception.getResponseCode().getStatus().value());
        return ResponseDto.of(exception.getResponseCode());
    }

    /**
     * 지원하지 않는 API 요청시
     */
    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseDto<Object> handleBusinessException(NoResourceFoundException exception) {
        response.setStatus(ResponseCode.INVALID_INPUT.getStatus().value());
        log.error("지원하지 않는 URI 요청 : {}", exception.getMessage());
        return ResponseDto.of(ResponseCode.INVALID_INPUT);
    }

    /**
     * 정의되지 않은 서버 오류
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseDto<Object> handleException(Exception exception) {
        response.setStatus(ResponseCode.SERVER_ERROR.getStatus().value());
        log.error("정의되지않은 서버 오류 : {}", exception.toString());
        return ResponseDto.of(ResponseCode.SERVER_ERROR);
    }
}
