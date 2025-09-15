package kr.co.easylogin.easyloginwebserver.order.dto.response;

import kr.co.easylogin.easyloginwebserver.order.domain.OrderHistory;
import lombok.Builder;

@Builder
public record OrderResultResponse(Long orderHistoryId, String orderCode, Long totalPrice) {

    public static OrderResultResponse of(OrderHistory orderHistory) {
        return OrderResultResponse.builder()
                                  .orderHistoryId(orderHistory.getId())
                                  .orderCode(orderHistory.getOrderCode())
                                  .totalPrice(orderHistory.getTotalPrice())
                                  .build();
    }
}
