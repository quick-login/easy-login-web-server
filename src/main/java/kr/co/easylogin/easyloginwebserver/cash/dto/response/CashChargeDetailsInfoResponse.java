package kr.co.easylogin.easyloginwebserver.cash.dto.response;

import java.time.LocalDateTime;
import kr.co.easylogin.easyloginwebserver.cash.CashChargeLog;
import kr.co.easylogin.easyloginwebserver.cash.value.CashChargeStatus;
import lombok.Builder;

@Builder
public record CashChargeDetailsInfoResponse(Long cashChargeLogId, Long chargeCash, CashChargeStatus status, LocalDateTime requestDate,
                                            Long memberId, String name) {

    public static CashChargeDetailsInfoResponse of(CashChargeLog cashChargeLog) {
        return CashChargeDetailsInfoResponse.builder()
                                            .cashChargeLogId(cashChargeLog.getId())
                                            .chargeCash(cashChargeLog.getChargeCash())
                                            .status(cashChargeLog.getStatus())
                                            .requestDate(cashChargeLog.getCreatedAt())
                                            .memberId(cashChargeLog.getMember().getId())
                                            .name(cashChargeLog.getMember().getName())
                                            .build();
    }
}
