package kr.co.easylogin.easyloginwebserver.common.security.jwt;

import org.springframework.util.StringUtils;

public class JwtBearerUtils {

    public static final String BEARER_PREFIX = "Bearer ";

    public static boolean hasBearerPrefix(String token) {
        return StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX);
    }

    /**
     * PREFIX 제거
     */
    public static String removePrefix(String token) {
        return hasBearerPrefix(token) ? token.substring(BEARER_PREFIX.length()) : token;
    }

    /**
     * PREFIX 추가
     */
    public static String addPrefix(String token) {
        return hasBearerPrefix(token) ? token : BEARER_PREFIX + token;
    }

}
