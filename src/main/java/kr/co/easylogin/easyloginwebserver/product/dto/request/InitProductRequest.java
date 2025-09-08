package kr.co.easylogin.easyloginwebserver.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.easylogin.easyloginwebserver.product.value.ProductType;
import lombok.Getter;

@Getter
public class InitProductRequest {

    @NotBlank(message = "이름은 비어있을 수 없습니다.")
    @Size(max = 50, message = "이름의 최대 길이는 50글자 이하여야 합니다.")
    private String name;

    @NotNull(message = "가격은 비어있을 수 없습니다.")
    private Long price;

    private Long discountRate;

    @NotNull(message = "상품 종류는 비어있을 수 없습니다.")
    private ProductType type;

    @NotNull(message = "상품 값은 비어있을 수 없습니다.")
    private Long value;
}
