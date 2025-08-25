package kr.co.easylogin.easyloginwebserver.cash.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CashChargeRequest {

    @NotNull(message = "충전금액은 비어있을 수 없습니다.")
    private Long chargeCash;
}
