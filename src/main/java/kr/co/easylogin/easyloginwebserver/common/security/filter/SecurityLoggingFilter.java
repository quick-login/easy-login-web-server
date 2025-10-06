package kr.co.easylogin.easyloginwebserver.common.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.co.easylogin.easyloginwebserver.common.log.RequestLogger;
import kr.co.easylogin.easyloginwebserver.config.SecurityConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityLoggingFilter extends OncePerRequestFilter {

    private final RequestLogger requestLogger;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        long start = System.currentTimeMillis();
        requestLogger.logRequest(request);

        try {
            filterChain.doFilter(request, response);
        } finally {
            Object exceptionFlag = request.getAttribute("exceptionOccurred");
            if (exceptionFlag == null || !(boolean) exceptionFlag) {
                requestLogger.logResponse(RequestLogger.getUuidFromRequest(request), request, start);
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();

        // 로그인, 로그아웃, 리프레시 경로만 필터 수행
        return !(path.equals(SecurityConfiguration.LOGIN_URL)
                 || path.equals(SecurityConfiguration.LOGOUT_URL)
                 || path.equals(SecurityConfiguration.REFRESH_URL));
    }
}

