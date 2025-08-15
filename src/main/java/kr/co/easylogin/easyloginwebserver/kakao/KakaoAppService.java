package kr.co.easylogin.easyloginwebserver.kakao;

import java.util.List;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.error.BusinessException;
import kr.co.easylogin.easyloginwebserver.common.utils.SecurityUtil;
import kr.co.easylogin.easyloginwebserver.kakao.request.RegisterKakaoBizAppRequest;
import kr.co.easylogin.easyloginwebserver.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoAppService {

    private final KakaoAppRepository kakaoAppRepository;
    private final SecurityUtil securityUtil;

    // 앱 등록시 리다이렉트 url이 null 이면 베이스 url 넣도록 수정
    @Value("${easylogin.base.redirectUrl}")
    private String baseRedirectUrl;

    @Transactional
    public void registerApp(RegisterKakaoBizAppRequest request) {
        Member member = securityUtil.getRequestMember();
        KakaoApp kakaoApp;
        List<KakaoApp> kakaoAppList = kakaoAppRepository.findByMember(member);
        if (kakaoAppList.size() >= member.getMaxKakaoAppCount()) {
            throw new BusinessException(ResponseCode.APP_REGISTRATION_LIMIT_EXCEEDED);
        }

        if (request.getRedirectUrl() == null || request.getRedirectUrl().isEmpty()) {
            kakaoApp = KakaoApp.of(member, request, baseRedirectUrl);
        } else {
            kakaoApp = KakaoApp.of(member, request);
        }

        kakaoAppRepository.save(kakaoApp);
    }
}
