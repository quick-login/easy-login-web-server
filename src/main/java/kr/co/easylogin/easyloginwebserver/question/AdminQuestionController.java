package kr.co.easylogin.easyloginwebserver.question;

import java.util.List;
import kr.co.easylogin.easyloginwebserver.common.dto.PageDto;
import kr.co.easylogin.easyloginwebserver.common.dto.PageResponseDto;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.question.dto.response.AdminQuestionListResponse;
import kr.co.easylogin.easyloginwebserver.question.value.QuestionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/api/v1/question")
public class AdminQuestionController {

    private final AdminQuestionService adminQuestionService;

    @GetMapping("/list")
    public PageResponseDto<List<AdminQuestionListResponse>> getQuestions(
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
        @RequestParam(name = "status", required = false) QuestionStatus status) {

        PageDto pageDto = PageDto.of(page, pageSize);
        List<AdminQuestionListResponse> result = adminQuestionService.getQuestions(pageDto, status);

        return PageResponseDto.of(ResponseCode.SUCCESS, result, pageDto);
    }
}
