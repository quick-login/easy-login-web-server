package kr.co.easylogin.easyloginwebserver.common.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.co.easylogin.easyloginwebserver.common.security.authentication.AccessTokenAuthentication;
import kr.co.easylogin.easyloginwebserver.common.security.jwt.JwtConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "AuthFilter")
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String authenticationHeader = request.getHeader(JwtConfig.ACCESS_TOKEN_HEADER);
        log.info("Access Token: {}", authenticationHeader);

        // 토큰이 없으면 인증 처리 없이 다음 필터로 전달
        if (!StringUtils.hasText(authenticationHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        // AccessToken 가져와서 인증객체(인증 전) 생성
        AccessTokenAuthentication preAuthentication = new AccessTokenAuthentication(authenticationHeader);
        // 생성된 인증객체 사용하여 인증 진행
        Authentication postAuthentication = authenticationManager.authenticate(preAuthentication);
        // 인증여부 시큐리티 컨텍스트 홀더에 저장
        SecurityContextHolder.getContext().setAuthentication(postAuthentication);

        // 다음필터 진행
        filterChain.doFilter(request, response);
    }
}
