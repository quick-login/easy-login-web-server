package kr.co.easylogin.easyloginwebserver.kakao;

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

        Long registerAppCount = kakaoAppRepository.countByMember(member);
        if (registerAppCount >= member.getMaxKakaoAppCount()) {
            log.error("등록 가능한 앱의 수룰 초과했습니다, 등록시도 회원 : {}", member.getId());
            throw new BusinessException(ResponseCode.APP_REGISTRATION_LIMIT_EXCEEDED);
        }

        kakaoAppRepository.findByAppId(request.getAppId()).ifPresent(app -> {
            log.error("이미 등록된 앱 아이디 : {} - {}, 등록시도 회원 : {}", app.getAppId(), app.getAppName(), member.getId());
            throw new BusinessException(ResponseCode.IS_PRESENT_KAKAO_APP);
        });

        if (request.getRedirectUrl() == null || request.getRedirectUrl().isEmpty()) {
            kakaoApp = KakaoApp.of(member, request, baseRedirectUrl);
        } else {
            kakaoApp = KakaoApp.of(member, request);
        }

        kakaoAppRepository.save(kakaoApp);
        log.info("카카오 앱 등록 성공 : {} - {}, 등록시도 회원 : {}", kakaoApp.getAppId(), kakaoApp.getAppName(), member.getId());
    }
}
