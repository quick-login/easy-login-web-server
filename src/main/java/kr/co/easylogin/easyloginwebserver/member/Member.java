package kr.co.easylogin.easyloginwebserver.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kr.co.easylogin.easyloginwebserver.common.BaseEntity;
import kr.co.easylogin.easyloginwebserver.member.dto.request.ModifyRequest;
import kr.co.easylogin.easyloginwebserver.member.dto.request.SignupRequest;
import kr.co.easylogin.easyloginwebserver.member.value.MemberRole;
import kr.co.easylogin.easyloginwebserver.member.value.MemberStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Slf4j
@Getter
public class Member extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 60)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Column(nullable = false)
    private Long remainCount;

    @Column(nullable = false)
    private Long maxKakaoAppCount;

    @Column(length = 20)
    private String kakaoId;

    @Column(nullable = false)
    private Long cash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    // 기본값 세팅
    protected Member() {
        this.role = MemberRole.USER;
        this.remainCount = 100L; // 회원가입시 기본 호출건수 제공
        this.maxKakaoAppCount = 1L; // 특별상황 아니면 카카오앱 하나만 등록 가능
        this.cash = 0L;
        this.status = MemberStatus.ACTIVE;
    }

    @Builder
    protected Member(String email, String name, String password, String kakaoId) {
        this();
        this.email = email;
        this.name = name;
        this.password = password;
        this.kakaoId = kakaoId;
    }

    public static Member of(SignupRequest request, String encryptPassword) {
        return Member.builder()
            .email(request.getEmail())
            .name(request.getName())
            .password(encryptPassword)
            .kakaoId(request.getKakaoId())
            .build();
    }

    public void modify(ModifyRequest request, String encPassword) {
        this.name = request.getName();
        this.password = encPassword;
    }
}
