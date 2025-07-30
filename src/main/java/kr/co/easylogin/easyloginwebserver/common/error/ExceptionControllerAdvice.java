package kr.co.easylogin.easyloginwebserver.common.error;

import kr.co.easylogin.easyloginwebserver.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ResponseDto<Object>> handleBusinessException(BusinessException exception) {
        ResponseDto<Object> responseDto = ResponseDto.of(exception.getResponseCode());
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
}
