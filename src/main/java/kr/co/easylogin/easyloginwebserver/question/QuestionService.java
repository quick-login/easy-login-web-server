package kr.co.easylogin.easyloginwebserver.question;

import kr.co.easylogin.easyloginwebserver.common.utils.SecurityUtil;
import kr.co.easylogin.easyloginwebserver.member.Member;
import kr.co.easylogin.easyloginwebserver.question.domain.Question;
import kr.co.easylogin.easyloginwebserver.question.dto.InitQuestionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final SecurityUtil securityUtil;
    private final QuestionRepository questionRepository;

    /**
     * 문의 요청하기
     */
    @Transactional
    public Question initQuestion(InitQuestionRequest request) {
        Member member = securityUtil.getRequestMember();
        Question question = Question.of(request, member);

        // 뭐 이후 여기에 슬랙발송이나 그런거 추가하면 될듯?
        // 나중에 S3 연동하고 첨부파일 저장도 연동하면 좋을듯 ㅇㅇ
        return questionRepository.save(question);
    }
}
