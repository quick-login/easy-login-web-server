package kr.co.easylogin.easyloginwebserver.question;

import jakarta.validation.Valid;
import java.util.List;
import kr.co.easylogin.easyloginwebserver.common.dto.PageDto;
import kr.co.easylogin.easyloginwebserver.common.dto.PageResponseDto;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.question.domain.Question;
import kr.co.easylogin.easyloginwebserver.question.dto.InitQuestionRequest;
import kr.co.easylogin.easyloginwebserver.question.dto.response.QuestionListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/question")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public void initQuestion(@Valid @RequestBody InitQuestionRequest request) {
        Question question = questionService.initQuestion(request);
        log.info("새로운 문의사항이 등록되었습니다. 문의번호 : {}, 요청회원 - {} - {}", question.getId(), question.getMember().getId(),
                 question.getMember().getName());
    }

    @GetMapping("/list")
    public PageResponseDto<List<QuestionListResponse>> getQuestionList(
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        PageDto pageDto = PageDto.of(page, pageSize);
        List<QuestionListResponse> result = questionService.getQuestions(pageDto);
        return PageResponseDto.of(ResponseCode.SUCCESS, result, pageDto);
    }

    @DeleteMapping("/cancel/{id}")
    public void cancel(@PathVariable(name = "id") Long id) {
        Question question = questionService.cancelQuestion(id);
        log.info("문의내역이 삭제되었습니다. 문의내역 ID : {}", question.getId());
    }
}
