package kr.co.easylogin.easyloginwebserver.common;

import kr.co.easylogin.easyloginwebserver.common.dto.PageResponseDto;
import kr.co.easylogin.easyloginwebserver.common.dto.ResponseDto;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 실패 응답(ExceptionControllerAdvice)는 이미 ResponseDto 감싸져서 해당 안됨
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 이미 ResponseDto로 감싸진 경우는 제외 + PageResponseDto로 감싸진 경우도 제외
        return !returnType.getParameterType().equals(ResponseDto.class) && !returnType.getParameterType().equals(PageResponseDto.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response
                                 ) {
        // 빈 응답은 기본 응답으로 처리
        if (body == null || returnType.getParameterType().equals(Void.class)) {
            return ResponseDto.of(ResponseCode.SUCCESS);
        }

        return ResponseDto.of(ResponseCode.SUCCESS, body);
    }
}
