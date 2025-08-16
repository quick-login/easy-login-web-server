package kr.co.easylogin.easyloginwebserver.kakao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.easylogin.easyloginwebserver.common.BaseEntity;
import kr.co.easylogin.easyloginwebserver.kakao.request.ModifyKakaoAppRequest;
import kr.co.easylogin.easyloginwebserver.kakao.request.RegisterKakaoAppRequest;
import kr.co.easylogin.easyloginwebserver.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoApp extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, unique = true)
    private Long appId;

    // 카카오에서는 최대 45글자인것같음
    @Column(nullable = false, length = 50)
    private String appName;

    // 카카오에서는 32글자 고정인것같은데 확실하진않으니 50자로 해둠
    @Column(nullable = false, length = 50)
    private String restKey;

    // 고객사로 정보 리다이렉트해줄 url
    @Column(nullable = false)
    private String redirectUrl;

    @Builder
    public KakaoApp(Member member, Long appId, String appName, String restKey, String redirectUrl) {
        this.member = member;
        this.appId = appId;
        this.appName = appName;
        this.restKey = restKey;
        this.redirectUrl = redirectUrl;
    }

    public static KakaoApp of(Member member, RegisterKakaoAppRequest request, String redirectUrl) {
        return KakaoApp.builder()
                       .member(member)
                       .appId(request.getAppId())
                       .appName(request.getAppName())
                       .restKey(request.getRestKey())
                       .redirectUrl(redirectUrl)
                       .build();
    }

    public static KakaoApp of(Member member, RegisterKakaoAppRequest request) {
        return KakaoApp.builder()
                       .member(member)
                       .appId(request.getAppId())
                       .appName(request.getAppName())
                       .restKey(request.getRestKey())
                       .redirectUrl(request.getRedirectUrl())
                       .build();
    }

    public void modifyAppInfo(ModifyKakaoAppRequest request) {
        this.appName = request.getAppName();
        this.restKey = request.getRestKey();
        this.redirectUrl = request.getRedirectUrl();
    }

}
