package kr.co.easylogin.easyloginwebserver.cash.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CashChargeStatus {
    REQUEST("요청"),
    PENDING_CONFIRMATION("확인 중"),
    CHARGE_COMPLETED("완료"),
    HIDDEN("숨김"),
    REJECTED("거절"),
    CANCELED("취소");

    private final String state;
}
