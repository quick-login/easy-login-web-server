package kr.co.easylogin.easyloginwebserver.product.dto;

import kr.co.easylogin.easyloginwebserver.product.domain.Product;
import lombok.Builder;

@Builder
public record ProductInfoResponse(Long product_id, String name, Long price, Long discountRate, Long finalPrice) {

    public static ProductInfoResponse of(Product product) {
        return ProductInfoResponse.builder()
                                  .product_id(product.getId())
                                  .name(product.getName())
                                  .price(product.getPrice())
                                  .discountRate(product.getDiscountRate())
                                  .finalPrice(product.getPrice() * (1 - product.getDiscountRate() / 100))
                                  .build();
    }
}
