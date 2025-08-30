package kr.co.easylogin.easyloginwebserver.cash;

import kr.co.easylogin.easyloginwebserver.cash.value.CashChargeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashChargeLogRepository extends JpaRepository<CashChargeLog, Long> {

    Page<CashChargeLog> findByMemberIdAndStatusNot(Long memberId, CashChargeStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"member"})
    Page<CashChargeLog> findByStatusNot(CashChargeStatus status, Pageable pageable);
}
