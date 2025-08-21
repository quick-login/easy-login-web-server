package kr.co.easylogin.easyloginwebserver.cash;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.easylogin.easyloginwebserver.cash.dto.request.CashChargeRequest;
import kr.co.easylogin.easyloginwebserver.cash.value.CashChargeStatus;
import kr.co.easylogin.easyloginwebserver.common.BaseEntity;
import kr.co.easylogin.easyloginwebserver.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Slf4j
@Getter
public class CashChargeLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Long chargeCash;

    @Enumerated(EnumType.STRING)
    private CashChargeStatus status;

    protected CashChargeLog() {
        this.status = CashChargeStatus.REQUEST;
    }

    @Builder
    protected CashChargeLog(Member member, Long chargeCash) {
        this();
        this.member = member;
        this.chargeCash = chargeCash;
    }

    public static CashChargeLog of(Member member, CashChargeRequest request) {
        return CashChargeLog.builder()
                            .member(member)
                            .chargeCash(request.getChargeCash())
                            .build();
    }
}
