package kr.co.easylogin.easyloginwebserver.notice;

import jakarta.validation.Valid;
import kr.co.easylogin.easyloginwebserver.notice.domain.Notice;
import kr.co.easylogin.easyloginwebserver.notice.dto.request.NoticeInitRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/api/v1/notice")
public class AdminNoticeController {

    private final AdminNoticeService adminNoticeService;

    @PostMapping
    public void notice(@Valid @RequestBody NoticeInitRequest request) {
        Notice notice = adminNoticeService.initNotice(request);
        log.info("{}번 공지사항이 등록되었습니다. 등록자 : {} {}", notice.getId(), notice.getMember().getId(), notice.getMember().getName());
    }

    @PatchMapping("/{id}")
    public void modifyNotice(@Valid @RequestBody NoticeInitRequest request,
                             @PathVariable(name = "id") Long id) {

        Notice notice = adminNoticeService.modifyNotice(request, id);
        log.info("{}번 공지가 수정되었습니다. 수정자 : {} {}", notice.getId(), notice.getMember().getId(), notice.getMember().getName());
    }
}
