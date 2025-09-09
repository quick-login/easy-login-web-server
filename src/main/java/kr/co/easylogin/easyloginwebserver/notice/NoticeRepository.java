package kr.co.easylogin.easyloginwebserver.notice;

import kr.co.easylogin.easyloginwebserver.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
