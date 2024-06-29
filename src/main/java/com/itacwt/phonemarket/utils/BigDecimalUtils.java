package com.itacwt.phonemarket.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtils {
    private final static BigDecimal PERCENTAGE_COEFFICIENT = new BigDecimal(100);

    public static BigDecimal addPercentage(BigDecimal price, BigDecimal percentage) {
        BigDecimal multiplier = percentage.divide(PERCENTAGE_COEFFICIENT, 2, RoundingMode.HALF_UP);
        BigDecimal increment = price.multiply(multiplier);
        return price.add(increment);
    }

    public static BigDecimal subtractPercentage(BigDecimal price, BigDecimal percentage) {
        BigDecimal multiplier = percentage.divide(PERCENTAGE_COEFFICIENT, 2, RoundingMode.HALF_UP);
        BigDecimal decrement = price.multiply(multiplier);
        return price.subtract(decrement);
    }

}
