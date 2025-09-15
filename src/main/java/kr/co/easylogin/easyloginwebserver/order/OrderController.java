package kr.co.easylogin.easyloginwebserver.order;

import jakarta.validation.Valid;
import java.text.DecimalFormat;
import java.util.List;
import kr.co.easylogin.easyloginwebserver.order.dto.request.CreateOrderRequest;
import kr.co.easylogin.easyloginwebserver.order.dto.response.OrderResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResultResponse createOrder(@Valid @RequestBody List<CreateOrderRequest> requests) {
        OrderResultResponse orderResult = orderService.createOrder(requests);
        log.info("상품 구매 완료 - 주문코드 : {}, 총 주문금액 : {}", orderResult.orderCode(), new DecimalFormat("#,###").format(orderResult.totalPrice()));
        return orderResult;
    }
}
