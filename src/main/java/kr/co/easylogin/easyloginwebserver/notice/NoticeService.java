package kr.co.easylogin.easyloginwebserver.notice;

import java.util.List;
import kr.co.easylogin.easyloginwebserver.common.dto.PageDto;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.error.BusinessException;
import kr.co.easylogin.easyloginwebserver.notice.domain.Notice;
import kr.co.easylogin.easyloginwebserver.notice.dto.response.NoticeDetailsResponse;
import kr.co.easylogin.easyloginwebserver.notice.dto.response.NoticeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<NoticeResponse> getFixedNotices() {
        List<Notice> notices = noticeRepository.findByFixedOrderByCreatedAtDesc(Boolean.TRUE);
        return notices.stream()
                      .map(NoticeResponse::of)
                      .toList();
    }

    public List<NoticeResponse> getNotices(PageDto pageDto) {
        PageRequest pageRequest = PageRequest.of(pageDto.getCurrentPage() - 1, pageDto.getPageSize(), Sort.by(Order.desc("createdAt")));

        Page<Notice> notices = noticeRepository.findAllWithMember(pageRequest);
        pageDto.updateTotalPagesAndElements(notices);
        pageDto.checkCurrentPage();

        return notices.stream()
                      .map(NoticeResponse::of)
                      .toList();
    }

    public NoticeDetailsResponse getNoticeById(Long id) {
        Notice notice = noticeRepository.findByIdWithMember(id)
                                        .orElseThrow(() -> new BusinessException(ResponseCode.NOTICE_NOT_FOUND));

        return NoticeDetailsResponse.of(notice);
    }
}
