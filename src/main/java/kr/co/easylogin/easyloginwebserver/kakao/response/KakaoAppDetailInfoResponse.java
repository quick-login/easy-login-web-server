package kr.co.easylogin.easyloginwebserver.kakao.response;

import kr.co.easylogin.easyloginwebserver.kakao.KakaoApp;
import lombok.Builder;

@Builder
public record KakaoAppDetailInfoResponse(Long appId, String appName, String restKey, String redirectUrl) {

    public static KakaoAppDetailInfoResponse of(KakaoApp kakaoApp) {
        return KakaoAppDetailInfoResponse.builder()
                                         .appId(kakaoApp.getAppId())
                                         .appName(kakaoApp.getAppName())
                                         .restKey(kakaoApp.getRestKey())
                                         .redirectUrl(kakaoApp.getRedirectUrl())
                                         .build();
    }
}
