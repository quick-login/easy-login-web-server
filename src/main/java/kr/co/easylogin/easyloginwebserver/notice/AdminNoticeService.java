package kr.co.easylogin.easyloginwebserver.notice;

import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.error.BusinessException;
import kr.co.easylogin.easyloginwebserver.common.utils.SecurityUtil;
import kr.co.easylogin.easyloginwebserver.member.Member;
import kr.co.easylogin.easyloginwebserver.notice.domain.Notice;
import kr.co.easylogin.easyloginwebserver.notice.dto.request.NoticeInitRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminNoticeService {

    private final NoticeRepository noticeRepository;
    private final SecurityUtil securityUtil;

    /**
     * 공지사항 등록
     */
    @Transactional
    public Notice initNotice(NoticeInitRequest request) {
        Member member = securityUtil.getRequestMember();
        Notice notice = Notice.of(request, member);

        return noticeRepository.save(notice);
    }

    /**
     * 공지사항 수정
     */
    @Transactional
    public Notice modifyNotice(NoticeInitRequest request, Long id) {
        Notice notice = noticeRepository.findById(id)
                                        .orElseThrow(() -> new BusinessException(ResponseCode.NOTICE_NOT_FOUND));
        notice.modify(request);
        return notice;
    }
}
