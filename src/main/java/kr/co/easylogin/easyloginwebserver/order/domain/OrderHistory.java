package kr.co.easylogin.easyloginwebserver.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import kr.co.easylogin.easyloginwebserver.common.BaseEntity;
import kr.co.easylogin.easyloginwebserver.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Slf4j
@Getter
public class OrderHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, length = 100)
    private String orderCode;

    @Column(nullable = false)
    private Long totalPrice;

    protected OrderHistory() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = now.format(formatter);

        this.orderCode = timestamp + "-" + UUID.randomUUID().toString().replace("-", "");
    }

    @Builder
    public OrderHistory(Long totalPrice, Member member) {
        this();
        this.member = member;
    }

    public void updateTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }
}
