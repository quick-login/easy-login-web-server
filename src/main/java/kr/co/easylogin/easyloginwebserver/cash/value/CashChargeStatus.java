package kr.co.easylogin.easyloginwebserver.cash.value;

import lombok.Getter;

@Getter
public enum CashChargeStatus {
    REQUEST, PENDING_CONFIRMATION, CHARGE_COMPLETED, HIDDEN
}
