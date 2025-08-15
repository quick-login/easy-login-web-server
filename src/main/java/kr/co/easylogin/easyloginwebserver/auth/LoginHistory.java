package kr.co.easylogin.easyloginwebserver.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.easylogin.easyloginwebserver.auth.value.LoginStatus;
import kr.co.easylogin.easyloginwebserver.common.BaseEntity;
import kr.co.easylogin.easyloginwebserver.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, length = 20)
    private String ipAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginStatus isSuccess;

    public LoginHistory(Member member, String remoteAddr, LoginStatus loginStatus) {
        this.member = member;
        this.ipAddress = remoteAddr;
        this.isSuccess = loginStatus;
    }
}
