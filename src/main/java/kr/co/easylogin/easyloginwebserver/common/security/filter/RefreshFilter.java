package kr.co.easylogin.easyloginwebserver.common.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.easylogin.easyloginwebserver.common.security.authentication.RefreshTokenAuthentication;
import kr.co.easylogin.easyloginwebserver.common.security.handler.RefreshSuccessHandler;
import kr.co.easylogin.easyloginwebserver.common.security.jwt.JwtConfig;
import kr.co.easylogin.easyloginwebserver.config.SecurityConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class RefreshFilter extends OncePerRequestFilter {

    private final RefreshSuccessHandler refreshSuccessHandler;
    private final AuthenticationManager authenticationManager;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authenticationHeader = request.getHeader(JwtConfig.REFRESH_TOKEN_HEADER);
        log.info("Refresh-Token : {}", authenticationHeader);

        // 리프레시 토큰이 없으면 다음 필터로 전달
        if (!StringUtils.hasText(authenticationHeader)) {
            filterChain.doFilter(request, response);
        }

        RefreshTokenAuthentication preAuthentication = new RefreshTokenAuthentication(authenticationHeader);
        Authentication postAuthentication = authenticationManager.authenticate(preAuthentication);
        SecurityContextHolder.getContext().setAuthentication(postAuthentication);

        refreshSuccessHandler.onAuthenticationSuccess(response, postAuthentication);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // "/api/v1/member/refresh" 경로가 아니라면 필터 로직을 수행하지 않음
        return !request.getServletPath().equals(SecurityConfiguration.REFRESH_URL);
    }
}
