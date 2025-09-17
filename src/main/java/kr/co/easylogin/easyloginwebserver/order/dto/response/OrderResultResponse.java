package kr.co.easylogin.easyloginwebserver.order.dto.response;

import java.time.format.DateTimeFormatter;
import kr.co.easylogin.easyloginwebserver.order.domain.OrderHistory;
import lombok.Builder;

@Builder
public record OrderResultResponse(String orderCode, Long totalPrice, String orderDate) {

    public static OrderResultResponse of(OrderHistory orderHistory) {
        return OrderResultResponse.builder()
                                  .orderCode(orderHistory.getOrderCode())
                                  .totalPrice(orderHistory.getTotalPrice())
                                  .orderDate(orderHistory.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                  .build();
    }
}
