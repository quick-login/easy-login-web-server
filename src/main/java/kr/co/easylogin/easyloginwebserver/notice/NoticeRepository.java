package kr.co.easylogin.easyloginwebserver.notice;

import java.util.List;
import kr.co.easylogin.easyloginwebserver.notice.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @EntityGraph(attributePaths = {"member"})
    List<Notice> findByFixedOrderByCreatedAtDesc(Boolean fixed);

    @Query("SELECT n FROM Notice n LEFT JOIN FETCH n.member")
    Page<Notice> findAllWithMember(Pageable pageable);
}
