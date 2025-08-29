package kr.co.easylogin.easyloginwebserver.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kr.co.easylogin.easyloginwebserver.common.BaseEntity;
import kr.co.easylogin.easyloginwebserver.product.dto.request.InitProductRequest;
import kr.co.easylogin.easyloginwebserver.product.value.ProductStatus;
import kr.co.easylogin.easyloginwebserver.product.value.ProductType;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Slf4j
@Getter
public class Product extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Long price;

    private Long discountRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType productType;

    @Column(nullable = false)
    private Long value;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    protected Product() {
        this.status = ProductStatus.SALE;
    }

    @Builder
    protected Product(String name, Long price, Long discountRate, ProductType productType, Long value) {
        this();
        this.name = name;
        this.price = price;
        this.discountRate = discountRate;
        this.productType = productType;
        this.value = value;
    }

    @Builder
    public static Product of(InitProductRequest request) {
        return Product.builder()
                      .name(request.getName())
                      .price(request.getPrice())
                      .discountRate(request.getDiscountRate() == null ? 0 : request.getDiscountRate())
                      .productType(request.getType())
                      .value(request.getValue())
                      .build();
    }

    public void changeStatus() {
        if (this.status.equals(ProductStatus.SALE)) {
            this.status = ProductStatus.STOP;
        } else if (this.status.equals(ProductStatus.STOP)) {
            this.status = ProductStatus.SALE;
        }
    }

    public void delete() {
        this.status = ProductStatus.DELETED;
    }
}
