package kr.co.easylogin.easyloginwebserver.kakao.response;

import kr.co.easylogin.easyloginwebserver.kakao.KakaoApp;
import lombok.Builder;

@Builder
public record KakaoAppInfoResponse(Long appId, String appName) {

    public static KakaoAppInfoResponse of(KakaoApp kakaoApp) {
        return KakaoAppInfoResponse.builder()
                                   .appId(kakaoApp.getAppId())
                                   .appName(kakaoApp.getAppName())
                                   .build();
    }
}
