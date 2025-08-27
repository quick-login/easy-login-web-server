package kr.co.easylogin.easyloginwebserver.product.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductType {

    API_REMAIN_COUNT_INCREMENT("API 호출 회수 증가"),
    KAKAO_APP_REGISTER_INCREMENT("카카오 앱 등록 개수 증가");

    private final String type;
}
