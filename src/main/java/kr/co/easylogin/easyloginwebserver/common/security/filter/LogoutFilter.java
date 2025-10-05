package kr.co.easylogin.easyloginwebserver.common.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.co.easylogin.easyloginwebserver.common.dto.ResponseDto;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.utils.RedisUtil;
import kr.co.easylogin.easyloginwebserver.config.SecurityConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class LogoutFilter extends OncePerRequestFilter {

    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            redisUtil.delete(username);
        }

        response.setStatus(HttpServletResponse.SC_OK);
        ObjectMapper objectMapper = new ObjectMapper();
        String responseJson = objectMapper.writeValueAsString(ResponseDto.of(ResponseCode.SUCCESS));
        response.getWriter().write(responseJson);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // /api/v1/member/logout 요청이 아니면 실행 X
        return !request.getServletPath().equals(SecurityConfiguration.LOGOUT_URL);
    }
}
