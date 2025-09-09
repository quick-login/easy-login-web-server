package kr.co.easylogin.easyloginwebserver.notice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.easylogin.easyloginwebserver.common.BaseEntity;
import kr.co.easylogin.easyloginwebserver.member.Member;
import kr.co.easylogin.easyloginwebserver.notice.dto.request.NoticeInitRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    private Boolean fixed;

    @Builder
    protected Notice(Member member, String title, String content, Boolean fixed) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.fixed = fixed != null ? fixed : false;
    }

    public static Notice of(NoticeInitRequest request, Member member) {
        return Notice.builder()
                     .member(member)
                     .title(request.getTitle())
                     .content(request.getContent())
                     .fixed(request.getFixed())
                     .build();
    }

    /**
     * 공지 수정
     */
    public void modify(NoticeInitRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.fixed = request.getFixed() != null ? request.getFixed() : false;
    }
}
