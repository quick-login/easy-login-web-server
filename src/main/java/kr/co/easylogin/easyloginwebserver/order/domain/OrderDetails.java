package kr.co.easylogin.easyloginwebserver.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.easylogin.easyloginwebserver.common.BaseEntity;
import kr.co.easylogin.easyloginwebserver.product.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Slf4j
@Getter
public class OrderDetails extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_history_id")
    private OrderHistory orderHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Long orderQuantity;

    @Column(nullable = false)
    private Long price;

    protected OrderDetails() {
    }

    @Builder
    public OrderDetails(OrderHistory orderHistory, Product product, Long orderQuantity, Long price) {
        this.orderHistory = orderHistory;
        this.product = product;
        this.orderQuantity = orderQuantity;
        this.price = price;
    }
}
