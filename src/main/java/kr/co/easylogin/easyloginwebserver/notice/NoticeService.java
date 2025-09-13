package kr.co.easylogin.easyloginwebserver.notice;

import java.util.List;
import kr.co.easylogin.easyloginwebserver.notice.domain.Notice;
import kr.co.easylogin.easyloginwebserver.notice.dto.response.NoticeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<NoticeResponse> getFixedNotices() {
        List<Notice> notices = noticeRepository.findByFixed(Boolean.TRUE);
        return notices.stream()
                      .map(NoticeResponse::of)
                      .toList();
    }

//    public List<NoticeResponse> getNotices(PageDto pageDto) {
//
//    }
}
