package com.itacwt.phonemarket.beans;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PurchaseRequest {
    private BigDecimal price;
}
