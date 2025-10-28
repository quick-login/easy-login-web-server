package kr.co.easylogin.easyloginwebserver.cash.dto.response;

import java.time.format.DateTimeFormatter;
import kr.co.easylogin.easyloginwebserver.cash.CashChargeLog;
import kr.co.easylogin.easyloginwebserver.cash.value.CashChargeStatus;
import lombok.Builder;

@Builder
public record CashChargeInfoResponse(Long cashChargeLogId, Long chargeCash, CashChargeStatus status, String requestDate) {

    public static CashChargeInfoResponse of(CashChargeLog cashChargeLog) {
        return CashChargeInfoResponse.builder()
                                     .cashChargeLogId(cashChargeLog.getId())
                                     .chargeCash(cashChargeLog.getChargeCash())
                                     .status(cashChargeLog.getStatus())
                                     .requestDate(cashChargeLog.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                     .build();
    }
}
