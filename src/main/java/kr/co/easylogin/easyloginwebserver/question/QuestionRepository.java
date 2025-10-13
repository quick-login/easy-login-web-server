package kr.co.easylogin.easyloginwebserver.question;

import java.util.Optional;
import kr.co.easylogin.easyloginwebserver.member.Member;
import kr.co.easylogin.easyloginwebserver.question.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findByMember(Member member, Pageable pageable);

    @Query("SELECT q FROM Question q LEFT JOIN FETCH q.member WHERE q.id = :id")
    Optional<Question> findByIdWithMemberId(Long id);
}
