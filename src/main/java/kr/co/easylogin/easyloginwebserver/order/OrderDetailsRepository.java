package kr.co.easylogin.easyloginwebserver.order;

import kr.co.easylogin.easyloginwebserver.order.domain.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {

}
