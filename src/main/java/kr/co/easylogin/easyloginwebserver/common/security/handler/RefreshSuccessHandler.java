package kr.co.easylogin.easyloginwebserver.common.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import kr.co.easylogin.easyloginwebserver.common.dto.ResponseDto;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.security.jwt.JwtBearerUtils;
import kr.co.easylogin.easyloginwebserver.common.security.jwt.JwtConfig;
import kr.co.easylogin.easyloginwebserver.common.security.jwt.JwtTokenFactory;
import kr.co.easylogin.easyloginwebserver.common.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshSuccessHandler {

    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;
    private final JwtTokenFactory jwtTokenFactory;

    public void onAuthenticationSuccess(HttpServletResponse response, Authentication authentication) throws IOException {
        this.getResponseDtoWithTokenInHeader(authentication, response); // 헤더에서 토큰을 추가한 응답 DTO 생성

        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // 미디어 타입 JSON 설정
        response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // UTF-8 설정 한글표시

        String result = objectMapper.writeValueAsString(ResponseDto.of(ResponseCode.SUCCESS));
        response.getWriter().write(result);
    }

    private void getResponseDtoWithTokenInHeader(Authentication authentication, HttpServletResponse response) {
        String name = authentication.getName();
        String role = List.of(authentication.getAuthorities()).get(0).toString();

        String accessToken = jwtTokenFactory.createAccessToken(name, role);
        String refreshToken = jwtTokenFactory.createRefreshToken(name, role);

        response.addHeader(JwtConfig.ACCESS_TOKEN_HEADER, JwtBearerUtils.addPrefix(accessToken));
        response.addHeader(JwtConfig.REFRESH_TOKEN_HEADER, JwtBearerUtils.addPrefix(refreshToken));

        redisUtil.set(name, refreshToken, JwtTokenFactory.REFRESH_TOKEN_EXPIRE);
    }
}
