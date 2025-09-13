package kr.co.easylogin.easyloginwebserver.notice.dto.response;

import java.time.LocalDate;
import kr.co.easylogin.easyloginwebserver.notice.domain.Notice;
import lombok.Builder;

@Builder
public record NoticeResponse(Long noticeId, String title, String name, LocalDate createdAt, Boolean fixed) {

    public static NoticeResponse of(Notice notice) {
        return NoticeResponse.builder()
                             .noticeId(notice.getId())
                             .title(notice.getTitle())
                             .name(notice.getMember().getName())
                             .createdAt(notice.getCreatedAt().toLocalDate())
                             .fixed(notice.getFixed())
                             .build();
    }
}
