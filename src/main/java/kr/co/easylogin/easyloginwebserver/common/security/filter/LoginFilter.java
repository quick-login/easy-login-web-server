package kr.co.easylogin.easyloginwebserver.common.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.error.BusinessException;
import kr.co.easylogin.easyloginwebserver.config.SecurityConfiguration;
import kr.co.easylogin.easyloginwebserver.member.dto.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final AuthenticationSuccessHandler loginSuccessHandler;

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
            return getAuthenticationManager().authenticate(preAuthentication);
        } catch (IOException e) {
            throw new BusinessException(ResponseCode.SERVER_ERROR);
        }
    }

    /**
     * 로그인 실패시 처리 로직
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("login failed message : {}", failed.getMessage());
        throw new BusinessException(ResponseCode.LOGIN_FAILED);
    }
}
