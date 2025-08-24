package kr.co.easylogin.easyloginwebserver.cash.dto.response;

import java.time.LocalDateTime;
import kr.co.easylogin.easyloginwebserver.cash.CashChargeLog;
import kr.co.easylogin.easyloginwebserver.cash.value.CashChargeStatus;
import lombok.Builder;

@Builder
public record CashChargeInfoResponse(Long cashChargeLogId, Long chargeCash, CashChargeStatus status, LocalDateTime requestDate) {

    public static CashChargeInfoResponse of(CashChargeLog cashChargeLog) {
        return CashChargeInfoResponse.builder()
                                     .cashChargeLogId(cashChargeLog.getId())
                                     .chargeCash(cashChargeLog.getChargeCash())
                                     .status(cashChargeLog.getStatus())
                                     .requestDate(cashChargeLog.getCreatedAt())
                                     .build();
    }
}
