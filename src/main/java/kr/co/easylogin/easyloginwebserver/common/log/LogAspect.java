package kr.co.easylogin.easyloginwebserver.common.log;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
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
public class LogAspect {

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

        log.info(">>> [LOG START] : {}", uuid);
        log.info(">>> [{}] {}", httpMethod, apiUrl);
        log.info(">>> Method: {}", methodName);
        log.info(">>> Params: {}", params);

        try {
            // 메서드 실행
            Object result = joinPoint.proceed();

            // 요청 처리 시간 계산
            long processingTime = System.currentTimeMillis() - startTime;

            // 응답 정보 로깅
            log.info("<<< [RESPONSE] Method: {}, URL: {}, Processing Time: {}ms", httpMethod, apiUrl, processingTime);
            log.info("<<< [LOG END] : {}", uuid);

            return result;
        } catch (Throwable t) {
            // 에러 발생 시 로깅
            long processingTime = System.currentTimeMillis() - startTime;
            log.error("!!! [ERROR] Processing Time: {}ms, Error: {}", processingTime, t.getMessage());
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

}
