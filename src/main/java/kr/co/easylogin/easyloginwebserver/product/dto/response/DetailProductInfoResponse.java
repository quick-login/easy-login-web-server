package kr.co.easylogin.easyloginwebserver.product.dto.response;

import kr.co.easylogin.easyloginwebserver.product.domain.Product;
import kr.co.easylogin.easyloginwebserver.product.value.ProductStatus;
import kr.co.easylogin.easyloginwebserver.product.value.ProductType;
import lombok.Builder;

@Builder
public record DetailProductInfoResponse(Long productId, Long price, Long discountRate, ProductType type, Long value, String typeDescription,
                                        ProductStatus status) {

    public static DetailProductInfoResponse of(Product product) {
        return DetailProductInfoResponse.builder()
                                        .productId(product.getId())
                                        .price(product.getPrice())
                                        .discountRate(product.getDiscountRate())
                                        .type(product.getProductType())
                                        .typeDescription(product.getProductType().getDescription())
                                        .value(product.getValue())
                                        .status(product.getStatus())
                                        .build();
    }
}
