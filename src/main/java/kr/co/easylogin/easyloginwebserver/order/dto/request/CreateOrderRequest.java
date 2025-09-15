package kr.co.easylogin.easyloginwebserver.order.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateOrderRequest {

    @NotNull(message = "상품 아이디는 비어있을 수 없습니다.")
    private Long productId;

    @NotNull(message = "상품 주문 수량은 비어있을 수 없습니다.")
    private Long orderQuantity;
}
