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
    Long totalFinalPrice,
    List<OrderDetailsProduct> orderProducts

) {

    public static OrderDetailsResultResponse of(OrderHistory history) {
        List<OrderDetailsProduct> orderDetailsProducts = history.getOrderDetails().stream().map(OrderDetailsProduct::of).toList();
        Long totalPrice = 0L;
        for (OrderDetailsProduct orderDetailsProduct : orderDetailsProducts) {
            totalPrice += orderDetailsProduct.price();
        }
        return OrderDetailsResultResponse.builder()
                                         .orderCode(history.getOrderCode())
                                         .orderDate(history.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                         .totalPrice(totalPrice)
                                         .totalFinalPrice(history.getTotalPrice())
                                         .orderProducts(history.getOrderDetails().stream().map(OrderDetailsProduct::of).toList())
                                         .build();
    }
}
