package kr.co.easylogin.easyloginwebserver.notice;

import java.util.List;
import kr.co.easylogin.easyloginwebserver.common.dto.PageDto;
import kr.co.easylogin.easyloginwebserver.common.dto.PageResponseDto;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.notice.dto.response.NoticeDetailsResponse;
import kr.co.easylogin.easyloginwebserver.notice.dto.response.NoticeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/fixed-notice")
    public List<NoticeResponse> getFixedNotices() {
        return noticeService.getFixedNotices();
    }

    @GetMapping("/list")
    public PageResponseDto<List<NoticeResponse>> getNoticeList(
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        PageDto pageDto = PageDto.of(page, pageSize);
        List<NoticeResponse> result = noticeService.getNotices(pageDto);
        return PageResponseDto.of(ResponseCode.SUCCESS, result, pageDto);
    }

    @GetMapping("/{id}")
    public NoticeDetailsResponse getNoticeDetails(@PathVariable(name = "id") Long id) {
        return noticeService.getNoticeById(id);
    }
}
