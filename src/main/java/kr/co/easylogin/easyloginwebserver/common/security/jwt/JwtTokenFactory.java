package kr.co.easylogin.easyloginwebserver.common.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenFactory {

    public static final Duration ACCESS_TOKEN_EXPIRE = Duration.ofMinutes(30); // Access Token 만료 시간
    public static final Duration REFRESH_TOKEN_EXPIRE = Duration.ofDays(14); // Refresh Token 만료 시간

    private final SecretKey secretKey;

    /**
     * Access Token 생성
     */
    public String createAccessToken(String subject, String role) {
        return createToken(subject, role, ACCESS_TOKEN_EXPIRE);
    }

    /**
     * Refresh Token 생성
     */
    public String createRefreshToken(String subject, String role) {
        return createToken(subject, role, REFRESH_TOKEN_EXPIRE);
    }

    private String createToken(String subject, String role, Duration expiration) {
        Instant now = Instant.now();
        Instant expire = now.plus(expiration);

        return Jwts.builder()
                   .subject(subject)
                   .claim(JwtConfig.AUTHORIZATION_KEY, role)
                   .expiration(Date.from(expire))
                   .issuedAt(Date.from(now))
                   .signWith(secretKey, SIG.HS512)
                   .compact();
    }
}
