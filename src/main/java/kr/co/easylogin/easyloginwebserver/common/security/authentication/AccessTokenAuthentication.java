package kr.co.easylogin.easyloginwebserver.common.security.authentication;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class AccessTokenAuthentication extends JwtAuthentication {

    /**
     * 인증 전
     */
    public AccessTokenAuthentication(String token) {
        super(token);
    }

    /**
     * 인증 후
     */
    public AccessTokenAuthentication(
        Object principal,
        String token,
        Collection<? extends GrantedAuthority> authorities) {
        
        super(principal, token, authorities);
    }
}
