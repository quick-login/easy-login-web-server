package kr.co.easylogin.easyloginwebserver.cash;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashChargeLogRepository extends JpaRepository<CashChargeLog, Long> {
    
}
