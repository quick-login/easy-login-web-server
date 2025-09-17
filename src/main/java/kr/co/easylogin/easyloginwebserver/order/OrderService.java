package kr.co.easylogin.easyloginwebserver.order;

import java.util.ArrayList;
import java.util.List;
import kr.co.easylogin.easyloginwebserver.common.dto.PageDto;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.error.BusinessException;
import kr.co.easylogin.easyloginwebserver.common.utils.SecurityUtil;
import kr.co.easylogin.easyloginwebserver.member.Member;
import kr.co.easylogin.easyloginwebserver.order.domain.OrderDetails;
import kr.co.easylogin.easyloginwebserver.order.domain.OrderHistory;
import kr.co.easylogin.easyloginwebserver.order.dto.request.CreateOrderRequest;
import kr.co.easylogin.easyloginwebserver.order.dto.response.OrderResultResponse;
import kr.co.easylogin.easyloginwebserver.product.ProductRepository;
import kr.co.easylogin.easyloginwebserver.product.domain.Product;
import kr.co.easylogin.easyloginwebserver.product.value.ProductStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderService {

    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final ProductRepository productRepository;
    private final SecurityUtil securityUtil;

    /**
     * 상품 주문
     */
    @Transactional
    public OrderResultResponse createOrder(List<CreateOrderRequest> requests) {

        if (requests.isEmpty()) {
            throw new BusinessException(ResponseCode.EMPTY_ORDER_LIST);
        }

        Member member = securityUtil.getRequestMember();

        List<OrderDetails> orderDetails = new ArrayList<>();
        long totalPrice = 0L;
        OrderHistory orderHistory = OrderHistory.builder()
                                                .member(member)
                                                .build();

        for (CreateOrderRequest request : requests) {
            Product product = productRepository.findById(request.getProductId())
                                               .orElseThrow(() -> new BusinessException(ResponseCode.PRODUCT_NOT_FOUND));

            // 판매중인 상품인지 검증
            checkProductSailing(product);
            // 할인가격 계산
            Long discountRate = product.getDiscountRate();
            // 계산식 = 원가 * (100 - 할인율) / 100
            long price = (product.getPrice() * request.getOrderQuantity()) * (100 - discountRate) / 100;

            OrderDetails orderDetail = OrderDetails.builder()
                                                   .orderHistory(orderHistory)
                                                   .product(product)
                                                   .orderQuantity(request.getOrderQuantity())
                                                   .price(price)
                                                   .discountRate(discountRate)
                                                   .build();

            orderDetails.add(orderDetail);
            totalPrice += price;
        }
        orderHistory.updateTotalPrice(totalPrice);

        // 회원 보유캐시 검증, 검증 완료시 포인트 차감
        checkMemberCash(member, totalPrice);

        orderHistoryRepository.save(orderHistory);
        orderDetailsRepository.saveAll(orderDetails);

        completeOrder(member, orderDetails);
        return OrderResultResponse.of(orderHistory);
    }

    /**
     * 판매중인 상품인지 체크
     */
    private void checkProductSailing(Product product) {
        if (!product.getStatus().equals(ProductStatus.SALE)) {
            throw new BusinessException(ResponseCode.NOT_ON_SALE);
        }
    }

    /**
     * 회원 보유포인트 검증
     */
    private void checkMemberCash(Member member, Long totalPrice) {
        if (member.getCash() >= totalPrice) {
            member.decreaseCash(totalPrice);
        } else {
            throw new BusinessException(ResponseCode.INSUFFICIENT_CASH);
        }
    }

    /**
     * 주문 성공시 상품 지급
     */
    private void completeOrder(Member member, List<OrderDetails> orderDetails) {
        for (OrderDetails orderDetail : orderDetails) {
            Product orderProduct = orderDetail.getProduct();
            long increment = orderProduct.getValue() * orderDetail.getOrderQuantity();

            switch (orderProduct.getProductType()) {
                case API_REMAIN_COUNT_INCREMENT -> {
                    member.increaseRemainCount(increment);
                    log.info("주문성공 === 주문회원 : {} - {}, 구매물품 : {} - {} x {}", member.getId(), member.getName(),
                             orderProduct.getId(), orderProduct.getName(), orderDetail.getOrderQuantity());
                }
                case KAKAO_APP_REGISTER_INCREMENT -> {
                    member.increaseMaxKakaoAppCount(increment);
                    log.info("주문성공 === 주문회원 : {} - {}, 구매물품 : {} - {} x {}", member.getId(), member.getName(),
                             orderProduct.getId(), orderProduct.getName(), orderDetail.getOrderQuantity());
                }
            }
        }
    }

    /**
     * 주문 목록 조회
     */
    public List<OrderResultResponse> getOrders(PageDto pageDto) {
        Member member = securityUtil.getRequestMember();
        PageRequest pageRequest = PageRequest.of(pageDto.getCurrentPage() - 1, pageDto.getPageSize(), Sort.by(Order.desc("createdAt")));

        Page<OrderHistory> orderHistories = orderHistoryRepository.findByMemberId(member.getId(), pageRequest);

        pageDto.updateTotalPagesAndElements(orderHistories);
        pageDto.checkCurrentPage();

        log.info("주문 목록 조회 - 조회 회원 : {} - {}", member.getId(), member.getName());

        return orderHistories.stream()
                             .map(OrderResultResponse::of)
                             .toList();
    }
}
