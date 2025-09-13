package kr.co.easylogin.easyloginwebserver.notice;

import java.util.List;
import kr.co.easylogin.easyloginwebserver.notice.domain.Notice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @EntityGraph(attributePaths = {"member"})
    List<Notice> findByFixed(Boolean fixed);
}
