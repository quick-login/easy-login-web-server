package kr.co.easylogin.easyloginwebserver.question;

import jakarta.validation.Valid;
import kr.co.easylogin.easyloginwebserver.question.domain.Question;
import kr.co.easylogin.easyloginwebserver.question.dto.InitQuestionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
