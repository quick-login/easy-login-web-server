package kr.co.easylogin.easyloginwebserver.order;

import jakarta.validation.Valid;
import java.text.DecimalFormat;
import java.util.List;
import kr.co.easylogin.easyloginwebserver.common.dto.PageDto;
import kr.co.easylogin.easyloginwebserver.common.dto.PageResponseDto;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.order.dto.request.CreateOrderRequest;
import kr.co.easylogin.easyloginwebserver.order.dto.response.OrderDetailsResultResponse;
import kr.co.easylogin.easyloginwebserver.order.dto.response.OrderResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/list")
    public PageResponseDto<List<OrderResultResponse>> getOrders(
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        PageDto pageDto = PageDto.of(page, pageSize);
        List<OrderResultResponse> results = orderService.getOrders(pageDto);

        return PageResponseDto.of(ResponseCode.SUCCESS, results, pageDto);
    }

    @GetMapping("/{orderCode}")
    public OrderDetailsResultResponse getOrderDetails(@PathVariable(name = "orderCode") String orderCode) {
        return orderService.getOrder(orderCode);
    }
}
