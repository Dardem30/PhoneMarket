package com.itacwt.phonemarket.entity;

import com.itacwt.phonemarket.enums.DiscountType;
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
    private DiscountType discountType;
    @Column(name = "discount_value")
    private BigDecimal discountValue;
}
