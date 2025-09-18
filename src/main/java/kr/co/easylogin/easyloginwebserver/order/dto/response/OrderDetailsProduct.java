package kr.co.easylogin.easyloginwebserver.order.dto.response;

import kr.co.easylogin.easyloginwebserver.order.domain.OrderDetails;
import kr.co.easylogin.easyloginwebserver.product.domain.Product;
import kr.co.easylogin.easyloginwebserver.product.value.ProductType;
import lombok.Builder;

@Builder
public record OrderDetailsProduct(
    String productName,
    ProductType productType,
    String productTypeDescription,
    Long value,
    Long orderQuantity,
    Long price,
    Long discountRate
) {

    public static OrderDetailsProduct of(OrderDetails orderDetails) {
        Product product = orderDetails.getProduct();
        return OrderDetailsProduct.builder()
                                  .productName(product.getName())
                                  .productType(product.getProductType())
                                  .productTypeDescription(product.getProductType().getDescription())
                                  .value(product.getValue())
                                  .orderQuantity(orderDetails.getOrderQuantity())
                                  .price(orderDetails.getPrice())
                                  .discountRate(orderDetails.getDiscountRate())
                                  .build();
    }
}
