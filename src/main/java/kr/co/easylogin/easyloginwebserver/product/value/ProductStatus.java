package kr.co.easylogin.easyloginwebserver.product.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {

    SALE("판매중"),
    STOP("판매 중지"),
    DELETED("삭제");

    private final String description;
}
