package com.itacwt.phonemarket.controller.request_forms;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalculatePriceForm {
    @NotNull(message = "Product id must be provided")
    private Long product;
    @NotNull(message = "Tax Number must be provided")
    @Size(min = 2, max = 20, message = "Invalid tax number")
    private String taxNumber;
    private String couponCode;
}
