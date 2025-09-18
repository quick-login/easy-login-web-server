package kr.co.easylogin.easyloginwebserver.order;

import kr.co.easylogin.easyloginwebserver.order.domain.OrderHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {

    Page<OrderHistory> findByMemberId(Long memberId, Pageable pageable);
}
