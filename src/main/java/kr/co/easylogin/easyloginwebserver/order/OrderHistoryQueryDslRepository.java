package kr.co.easylogin.easyloginwebserver.order;

import static kr.co.easylogin.easyloginwebserver.order.domain.QOrderDetails.orderDetails;
import static kr.co.easylogin.easyloginwebserver.order.domain.QOrderHistory.orderHistory;
import static kr.co.easylogin.easyloginwebserver.product.domain.QProduct.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.error.BusinessException;
import kr.co.easylogin.easyloginwebserver.order.domain.OrderHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderHistoryQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 주문 코드로 주문내역 조회 (With, 주문상세, 제품정보)
     * - left join orderDetails(주문상세)
     * - left join product(제품정보)
     * - 조회조건 : 주문 코드
     */
    public OrderHistory findByOrderCodeWithOrderDetailsAndProduct(String orderCode) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(orderHistory)
                                                  .leftJoin(orderDetails, orderDetails).fetchJoin()
                                                  .leftJoin(orderDetails.product, product).fetchJoin()
                                                  .where(orderHistory.orderCode.eq(orderCode))
                                                  .fetchOne())
                       .orElseThrow(() -> new BusinessException(ResponseCode.ORDER_HISTORY_NOT_FOUND));
    }
}
