package kr.co.easylogin.easyloginwebserver.kakao;

import java.util.List;
import kr.co.easylogin.easyloginwebserver.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KakaoAppRepository extends JpaRepository<KakaoApp, Long> {

    List<KakaoApp> findByMember(Member member);
}
