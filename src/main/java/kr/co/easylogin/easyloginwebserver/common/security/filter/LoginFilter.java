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

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            // -Djava.net.preferIPv4Stack=true VM 옵션에 추가해야 IPv4 형태로 뽑아옴
            ip = request.getRemoteAddr();
        }

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
