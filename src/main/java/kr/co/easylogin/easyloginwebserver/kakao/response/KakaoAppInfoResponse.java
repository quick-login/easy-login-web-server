package kr.co.easylogin.easyloginwebserver.kakao.response;

import kr.co.easylogin.easyloginwebserver.kakao.KakaoApp;
import lombok.Builder;

@Builder
public record KakaoAppInfoResponse(Long appId, String appName, String restKey, String redirectUrl) {

    public static KakaoAppInfoResponse of(KakaoApp kakaoApp) {
        return KakaoAppInfoResponse.builder()
                                   .appId(kakaoApp.getAppId())
                                   .appName(kakaoApp.getAppName())
                                   .restKey(kakaoApp.getRestKey())
                                   .redirectUrl(kakaoApp.getRedirectUrl())
                                   .build();
    }
}
