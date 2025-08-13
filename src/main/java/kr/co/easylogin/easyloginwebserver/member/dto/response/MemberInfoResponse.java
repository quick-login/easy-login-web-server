package kr.co.easylogin.easyloginwebserver.member.dto.response;

import kr.co.easylogin.easyloginwebserver.member.Member;
import lombok.Builder;

@Builder
public record MemberInfoResponse(String name, String email, Long cash, Long remainCount, Long maxKakaoAppCount) {

    public static MemberInfoResponse of(Member member) {
        return MemberInfoResponse.builder()
                                 .name(member.getName())
                                 .email(member.getEmail())
                                 .cash(member.getCash())
                                 .remainCount(member.getRemainCount())
                                 .maxKakaoAppCount(member.getMaxKakaoAppCount())
                                 .build();
    }
}
