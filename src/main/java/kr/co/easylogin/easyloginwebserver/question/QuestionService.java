package kr.co.easylogin.easyloginwebserver.question;

import java.util.List;
import kr.co.easylogin.easyloginwebserver.common.dto.PageDto;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.error.BusinessException;
import kr.co.easylogin.easyloginwebserver.common.utils.SecurityUtil;
import kr.co.easylogin.easyloginwebserver.member.Member;
import kr.co.easylogin.easyloginwebserver.question.domain.Question;
import kr.co.easylogin.easyloginwebserver.question.dto.request.InitQuestionRequest;
import kr.co.easylogin.easyloginwebserver.question.dto.response.QuestionInfoResponse;
import kr.co.easylogin.easyloginwebserver.question.dto.response.QuestionListResponse;
import kr.co.easylogin.easyloginwebserver.question.value.QuestionStatus;
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

    /**
     * 문의 내역 조회
     */
    public List<QuestionListResponse> getQuestions(PageDto pageDto) {
        Member member = securityUtil.getRequestMember();
        PageRequest pageRequest = PageRequest.of(pageDto.getCurrentPage() - 1, pageDto.getPageSize(), Sort.by(Order.desc("createdAt")));

        Page<Question> questions = questionRepository.findByMember(member, pageRequest);

        pageDto.updateTotalPagesAndElements(questions);
        pageDto.checkCurrentPage();

        log.info("회원 문의 내역 조회 - 조회 회원 : {} {}", member.getId(), member.getName());

        return questions.stream()
                        .map(QuestionListResponse::of)
                        .toList();
    }

    /**
     * 문의 취소하기
     */
    @Transactional
    public Question cancelQuestion(Long id) {
        Member member = securityUtil.getRequestMember();
        Question question = questionRepository.findById(id)
                                              .orElseThrow(() -> new BusinessException(ResponseCode.QUESTION_NOT_FOUND));

        checkPermission(question, member);
        checkStatus(question);

        questionRepository.deleteById(id);
        return question;
    }

    /**
     * 문의 상세 내용 조회
     */
    public QuestionInfoResponse getQuestionInfo(Long id) {
        Member member = securityUtil.getRequestMember();
        Question question = questionRepository.findById(id)
                                              .orElseThrow(() -> new BusinessException(ResponseCode.QUESTION_NOT_FOUND));

        checkPermission(question, member);
        log.info("문의 상세 내용 조회 {} : 조회 회원 - {} {}", question.getId(), member.getId(), member.getName());
        return QuestionInfoResponse.of(question);
    }

    /**
     * 문의 회원 검사
     */
    private void checkPermission(Question question, Member member) {
        if (!question.getMember().equals(member)) {
            log.info("조회권한이 없는 문의 조회 : {}, 조회시도 회원 : {}", question.getId(), member.getId());
            throw new BusinessException(ResponseCode.API_FORBIDDEN);
        }
    }

    /**
     * 삭제 가능한 상태인지 검사
     */
    private void checkStatus(Question question) {
        if (question.getStatus().equals(QuestionStatus.COMPLETED)) {
            throw new BusinessException(ResponseCode.CHANGE_STATUS_NOT_ALLOWED);
        }
    }
}
