package kr.co.easylogin.easyloginwebserver.order.dto.response;

import java.time.format.DateTimeFormatter;
import java.util.List;
import kr.co.easylogin.easyloginwebserver.order.domain.OrderHistory;
import lombok.Builder;

@Builder
public record OrderDetailsResultResponse(
    String orderCode,
    String orderDate,
    Long totalPrice,
    List<OrderDetailsProduct> orderProducts

) {

    public static OrderDetailsResultResponse of(OrderHistory history) {
        return OrderDetailsResultResponse.builder()
                                         .orderCode(history.getOrderCode())
                                         .orderDate(history.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                         .totalPrice(history.getTotalPrice())
                                         .orderProducts(history.getOrderDetails().stream().map(OrderDetailsProduct::of).toList())
                                         .build();
    }
}
