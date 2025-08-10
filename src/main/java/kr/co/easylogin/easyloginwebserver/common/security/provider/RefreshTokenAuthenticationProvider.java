package kr.co.easylogin.easyloginwebserver.common.security.provider;

import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.error.BusinessException;
import kr.co.easylogin.easyloginwebserver.common.security.authentication.RefreshTokenAuthentication;
import kr.co.easylogin.easyloginwebserver.common.security.jwt.JwtBearerUtils;
import kr.co.easylogin.easyloginwebserver.common.security.jwt.JwtClaimsExtractor;
import kr.co.easylogin.easyloginwebserver.common.security.jwt.JwtStatus;
import kr.co.easylogin.easyloginwebserver.common.security.jwt.JwtValidator;
import kr.co.easylogin.easyloginwebserver.common.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenAuthenticationProvider implements AuthenticationProvider {

    private final RedisUtil redisUtil;
    private final JwtValidator jwtValidator;
    private final JwtClaimsExtractor jwtClaimsExtractor;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = getTokenWithoutBearer(authentication);

        validateTokenStatus(token);

        String subject = jwtClaimsExtractor.getSubject(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(subject);

        validateSameRefreshToken(subject, token);

        return new RefreshTokenAuthentication(userDetails, token, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RefreshTokenAuthentication.class.isAssignableFrom(authentication);
    }

    /**
     * Bearer PREFIX 제거하고 토근 가져오기
     */
    private String getTokenWithoutBearer(Authentication authentication) {
        String tokenWithBearer = (String) authentication.getCredentials();
        return JwtBearerUtils.removePrefix(tokenWithBearer);
    }

    /**
     * JWT 액세스토큰 유효성 검증
     */
    private void validateTokenStatus(String token) {
        JwtStatus tokenStatus = jwtValidator.validate(token);

        if (tokenStatus.equals(JwtStatus.INVALID)) {
            throw new BusinessException(ResponseCode.INVALID_TOKEN);
        }

        if (tokenStatus.equals(JwtStatus.EXPIRED)) {
            throw new BusinessException(ResponseCode.EXPIRED_REFRESH_TOKEN);
        }
    }

    private void validateSameRefreshToken(String email, String token) {
        String tokenInRedis = redisUtil.get(email, String.class)
                                       .orElseThrow(() -> new BusinessException(ResponseCode.LOGIN_REQUIRED));

        if (!tokenInRedis.equals(token)) { // 다른 리프레쉬 토큰
            throw new BusinessException(ResponseCode.INVALID_TOKEN);
        }
    }
}
