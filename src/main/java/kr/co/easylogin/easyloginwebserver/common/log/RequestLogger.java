package kr.co.easylogin.easyloginwebserver.common.log;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestLogger {

    private static final String LOG_UUID_KEY = "LOG_UUID";

    public static String getUuidFromRequest(HttpServletRequest request) {
        Object uuid = request.getAttribute(LOG_UUID_KEY);
        return uuid != null ? uuid.toString() : UUID.randomUUID().toString();
    }

    public void logRequest(HttpServletRequest request) {
        String uuid = UUID.randomUUID().toString();
        request.setAttribute(LOG_UUID_KEY, uuid); // 필터나 컨트롤러 등에서 재사용 가능하게 저장

        String httpMethod = request.getMethod();
        String apiUrl = request.getRequestURI();
        String params = getRequestParams(request);

        log.info(">>> [LOG START] : {}", uuid);
        log.info(">>> [IP] : {}", request.getRemoteAddr());
        log.info(">>> [{}] {}", httpMethod, apiUrl);
        log.info(">>> Params: {}", params);
        log.info(">>> Body : Filter 바디값 체크 X");
    }

    public void logResponse(String uuid, HttpServletRequest request, long startTime) {
        long processingTime = System.currentTimeMillis() - startTime;
        log.info("<<< [RESPONSE] URL: {}, Processing Time: {}ms", request.getRequestURI(), processingTime);
        log.info("<<< [LOG END] : {}\n", uuid);
    }

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
