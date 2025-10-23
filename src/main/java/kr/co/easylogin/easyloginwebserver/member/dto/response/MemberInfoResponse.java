package kr.co.easylogin.easyloginwebserver.member.dto.response;

import kr.co.easylogin.easyloginwebserver.member.Member;
import kr.co.easylogin.easyloginwebserver.member.value.MemberRole;
import lombok.Builder;

@Builder
public record MemberInfoResponse(String name, String email, Long cash, Long remainCount, Long maxKakaoAppCount, MemberRole role) {

    public static MemberInfoResponse of(Member member) {
        return MemberInfoResponse.builder()
                                 .name(member.getName())
                                 .email(member.getEmail())
                                 .cash(member.getCash())
                                 .remainCount(member.getRemainCount())
                                 .maxKakaoAppCount(member.getMaxKakaoAppCount())
                                 .role(member.getRole())
                                 .build();
    }
}
