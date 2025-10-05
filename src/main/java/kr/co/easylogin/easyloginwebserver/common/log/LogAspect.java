package kr.co.easylogin.easyloginwebserver.common.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final ObjectMapper objectMapper;

    // Controller 패키지의 모든 메서드를 대상으로 Pointcut 정의
    @Pointcut("execution(* kr.co.easylogin.easyloginwebserver..*Controller.*(..))")
    public void controllerMethods() {
    }

    @Around("controllerMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 요청 정보 가져오기
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        long startTime = System.currentTimeMillis();

        // 요청 정보 로깅
        String uuid = UUID.randomUUID().toString();
        String httpMethod = request.getMethod();
        String apiUrl = request.getRequestURI();
        String methodName = joinPoint.getSignature().toShortString();
        String params = getRequestParams(request);
        String body = getRequestBody(joinPoint.getArgs());

        log.info(">>> [LOG START] : {}", uuid);
        log.info(">>> [IP] : {}", request.getRemoteAddr());
        log.info(">>> [{}] {}", httpMethod, apiUrl);
        log.info(">>> Method: {}", methodName);
        log.info(">>> Params: {}", params);
        log.info(">>> Body : {}", body);

        try {
            // 메서드 실행
            Object result = joinPoint.proceed();

            // 요청 처리 시간 계산
            long processingTime = System.currentTimeMillis() - startTime;

            // 응답 정보 로깅
            log.info("<<< [RESPONSE] Method: {}, URL: {}, Processing Time: {}ms", httpMethod, apiUrl, processingTime);
            log.info("<<< [LOG END] : {}\n", uuid);

            return result;
        } catch (Throwable t) {
            // 에러 발생 시 로깅
            long processingTime = System.currentTimeMillis() - startTime;
            log.error("!!! [ERROR] Processing Time: {}ms, Error: {}\n", processingTime, t.getMessage());
            throw t;
        }
    }

    // 요청 파라미터를 문자열로 변환
    private String getRequestParams(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        if (paramMap.isEmpty()) {
            return "{}";
        }
        return paramMap.entrySet().stream()
                       .map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue()))
                       .collect(Collectors.joining(", ", "{", "}"));
    }

    // 요청 본문을 JSON 문자열로 변환
    private String getRequestBody(Object[] args) {
        if (args == null || args.length == 0) {
            return "{}";
        }
        try {
            // 민감 필드 필터링을 위한 ObjectMapper 설정
            ObjectMapper maskedMapper = objectMapper.copy();
            FilterProvider filters = new SimpleFilterProvider()
                .addFilter("sensitiveFilter", SimpleBeanPropertyFilter.serializeAllExcept("password", "passwordCheck"));
            maskedMapper.setFilterProvider(filters);

            // 첫 번째 인자를 본문으로 간주
            Object bodyObject = args[0];
            return maskedMapper.writeValueAsString(bodyObject);
        } catch (Exception e) {
            return "body 값 파싱 실패: " + e.getMessage();
        }
    }

}
