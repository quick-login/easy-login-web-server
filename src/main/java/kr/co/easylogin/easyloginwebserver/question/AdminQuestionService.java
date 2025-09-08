package kr.co.easylogin.easyloginwebserver.question;

import static kr.co.easylogin.easyloginwebserver.question.domain.QQuestion.question;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kr.co.easylogin.easyloginwebserver.common.dto.PageDto;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.error.BusinessException;
import kr.co.easylogin.easyloginwebserver.common.utils.SecurityUtil;
import kr.co.easylogin.easyloginwebserver.member.Member;
import kr.co.easylogin.easyloginwebserver.member.QMember;
import kr.co.easylogin.easyloginwebserver.question.domain.Question;
import kr.co.easylogin.easyloginwebserver.question.dto.response.AdminQuestionListResponse;
import kr.co.easylogin.easyloginwebserver.question.dto.response.QuestionInfoResponse;
import kr.co.easylogin.easyloginwebserver.question.value.QuestionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminQuestionService {

    private final QuestionRepository questionRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final SecurityUtil securityUtil;

    /**
     * 문의 내역 조회
     * 상태값 조회로 동적쿼리 만들어놨는데..
     * 코드가 너무 더러워서 나중에 queryQuestionRepository 로 빼던가..
     * 필요없으면 제거하던가 할듯?
     */
    public List<AdminQuestionListResponse> getQuestions(PageDto pageDto, QuestionStatus status) {
        Member member = securityUtil.getRequestMember();
        PageRequest pageRequest = PageRequest.of(pageDto.getCurrentPage() - 1, pageDto.getPageSize(), Sort.by(Order.desc("createdAt")));

        List<AdminQuestionListResponse> questions = jpaQueryFactory.selectFrom(question)
                                                                   .leftJoin(question.member, QMember.member).fetchJoin()
                                                                   .where(statusEq(status))
                                                                   .offset(pageRequest.getOffset())
                                                                   .limit(pageRequest.getPageSize())
                                                                   .orderBy(question.createdAt.desc())
                                                                   .fetch()
                                                                   .stream().map(AdminQuestionListResponse::of)
                                                                   .toList();

        Long total = jpaQueryFactory.select(question.count())
                                    .from(question)
                                    .where(statusEq(status))
                                    .orderBy(question.createdAt.desc())
                                    .fetchOne();

        Page<AdminQuestionListResponse> result = new PageImpl<>(questions, pageRequest, total);
        pageDto.updateTotalPagesAndElements(result);

        log.info("관리자 : 문의 내역 목록 조회 - 조회 관리자 : {}", member.getName());
        return questions;
    }

    /**
     * status 비교 동적쿼리
     */
    private BooleanExpression statusEq(QuestionStatus status) {
        return status != null ? question.status.eq(status) : null;
    }

    /**
     * 문의 상세 내용 조회
     */
    public QuestionInfoResponse getQuestionInfo(Long id) {
        Member member = securityUtil.getRequestMember();
        Question question = questionRepository.findById(id)
                                              .orElseThrow(() -> new BusinessException(ResponseCode.QUESTION_NOT_FOUND));

        log.info("관리자 - 문의 상세 내용 조회 {} : 조회 회원 - {} {}", question.getId(), member.getId(), member.getName());
        return QuestionInfoResponse.of(question);
    }
}
