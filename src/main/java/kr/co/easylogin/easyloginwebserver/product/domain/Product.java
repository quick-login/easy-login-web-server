package kr.co.easylogin.easyloginwebserver.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import kr.co.easylogin.easyloginwebserver.common.BaseEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Slf4j
@Getter
public class Product extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Long price;

    private Long discountRate;
}
