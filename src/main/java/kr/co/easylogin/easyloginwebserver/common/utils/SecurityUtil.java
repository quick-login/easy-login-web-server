package kr.co.easylogin.easyloginwebserver.common.utils;

import kr.co.easylogin.easyloginwebserver.common.security.userDetils.UserDetailsImpl;
import kr.co.easylogin.easyloginwebserver.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    /**
     * SecurityContext 에서 email 꺼내서 멤버객체 조회
     */
    public Member getRequestMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        return principal.member();
    }

}
