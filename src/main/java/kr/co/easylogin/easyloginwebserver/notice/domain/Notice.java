package kr.co.easylogin.easyloginwebserver.notice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.easylogin.easyloginwebserver.common.BaseEntity;
import kr.co.easylogin.easyloginwebserver.member.Member;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Slf4j
@Getter
public class Notice extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    private Boolean fixed;
}
