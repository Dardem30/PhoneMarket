package com.itacwt.phonemarket.controller.forms;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalculatePriceForm {
    @NotNull(message = "Product id must be provided")
    private Long product;
    @NotNull(message = "Tax Number must be provided")
    @Min(value = 2, message = "Tax number is too short")
    private String taxNumber;
    private String couponCode;
}
