package kr.co.easylogin.easyloginwebserver.notice.dto.response;

import java.time.format.DateTimeFormatter;
import kr.co.easylogin.easyloginwebserver.notice.domain.Notice;
import lombok.Builder;

@Builder
public record NoticeDetailsResponse(Long noticeId, String title, String name, String content, String createdAt) {

    public static NoticeDetailsResponse of(Notice notice) {
        return NoticeDetailsResponse.builder()
                                    .noticeId(notice.getId())
                                    .title(notice.getTitle())
                                    .content(notice.getContent())
                                    .name(notice.getMember().getName())
                                    .createdAt(notice.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                    .build();
    }
}
