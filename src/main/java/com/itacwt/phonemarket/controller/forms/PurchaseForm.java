package com.itacwt.phonemarket.controller.forms;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseForm extends CalculatePriceForm {
    @NotNull
    private String paymentProcessor;
}
