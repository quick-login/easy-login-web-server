package kr.co.easylogin.easyloginwebserver.common.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.co.easylogin.easyloginwebserver.auth.LoginHistory;
import kr.co.easylogin.easyloginwebserver.auth.LoginHistoryRepository;
import kr.co.easylogin.easyloginwebserver.auth.value.LoginStatus;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.error.BusinessException;
import kr.co.easylogin.easyloginwebserver.common.security.userDetils.UserDetailsImpl;
import kr.co.easylogin.easyloginwebserver.config.SecurityConfiguration;
import kr.co.easylogin.easyloginwebserver.member.Member;
import kr.co.easylogin.easyloginwebserver.member.dto.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final AuthenticationSuccessHandler loginSuccessHandler;
    private final LoginHistoryRepository loginHistoryRepository;

    @PostConstruct
    public void init() {
        this.setAuthenticationSuccessHandler(loginSuccessHandler);
        setFilterProcessesUrl(SecurityConfiguration.LOGIN_URL); // /api/v1/member/login 요청에서만 필터 실행
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // 요청 JSON 파싱
            LoginRequest req = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

            // 인증 처리 로직
            Authentication preAuthentication = new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword(), null);
            Authentication authentication = getAuthenticationManager().authenticate(preAuthentication);

            if (authentication.isAuthenticated()) {
                UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
                Member member = principal.member();

                LoginHistory loginHistory = new LoginHistory(member, getClientIP(request), LoginStatus.SUCCESS);
                loginHistoryRepository.save(loginHistory);
            } else {
                UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
                Member member = principal.member();

                LoginHistory loginHistory = new LoginHistory(member, getClientIP(request), LoginStatus.FAIL);
                loginHistoryRepository.save(loginHistory);
                throw new BusinessException(ResponseCode.INVALID_LOGIN_INFO);
            }

            return authentication;
        } catch (IOException e) {
            throw new BusinessException(ResponseCode.SERVER_ERROR);
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        log.info("X-Forwarded-For : {}", ip);

        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            // 여러 IP가 있을 때 첫 번째(클라이언트 IP)만 추출
            ip = ip.split(",")[0].trim();
            log.info("Extracted Client IP from X-Forwarded-For: {}", ip);
            return ip;
        }

        // 기존 다른 헤더 체크 (필요시 유지)
        ip = request.getHeader("Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("HTTP_CLIENT_IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            ip = ip.split(",")[0].trim();
            return ip;
        }

        ip = request.getRemoteAddr();
        log.info("Fallback to getRemoteAddr(): {}", ip);
        return ip;
    }

    /**
     * 로그인 실패시 처리 로직
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.info("login failed message : {}", failed.getMessage());
        throw new BusinessException(ResponseCode.LOGIN_FAILED);
    }
}
