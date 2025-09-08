package kr.co.easylogin.easyloginwebserver.question.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import kr.co.easylogin.easyloginwebserver.common.BaseEntity;
import kr.co.easylogin.easyloginwebserver.member.Member;
import kr.co.easylogin.easyloginwebserver.question.dto.request.AnswerRequest;
import kr.co.easylogin.easyloginwebserver.question.dto.request.InitQuestionRequest;
import kr.co.easylogin.easyloginwebserver.question.value.QuestionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Slf4j
@Getter
public class Question extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(length = 1000)
    private String answer;

    @Column(nullable = true)
    private LocalDateTime answeredDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionStatus status;

    protected Question() {
        this.status = QuestionStatus.WAITING;
    }

    @Builder
    protected Question(Member member, String title, String content) {
        this();
        this.member = member;
        this.title = title;
        this.content = content;
    }

    public static Question of(InitQuestionRequest request, Member member) {
        return Question.builder()
                       .member(member)
                       .title(request.getTitle())
                       .content(request.getContent())
                       .build();
    }

    public void createAnswer(AnswerRequest request) {
        this.answer = request.getAnswer();
        this.answeredDate = LocalDateTime.now();
        this.status = QuestionStatus.COMPLETED;
    }
}
