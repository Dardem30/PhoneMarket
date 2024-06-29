package com.itacwt.phonemarket.entity;

import com.itacwt.phonemarket.beans.enums.DiscountType;
import com.itacwt.phonemarket.utils.BigDecimalUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "coupons")
@Getter
@Setter
public class Coupon {
    @Id
    @Column(name = "coupon_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "code")
    private String code;
    @Column(name = "discount_type")
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    @Column(name = "discount_value")
    private BigDecimal discountValue;

    public BigDecimal apply(BigDecimal price) {
        if (DiscountType.fixed.equals(getDiscountType())) {
            price = price.subtract(getDiscountValue());
        } else if (DiscountType.percent.equals(getDiscountType())) {
            price = BigDecimalUtils.subtractPercentage(price, getDiscountValue());
        }
        return price;
    }
}
