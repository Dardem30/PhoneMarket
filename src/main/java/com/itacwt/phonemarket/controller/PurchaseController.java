package com.itacwt.phonemarket.controller;

import com.itacwt.phonemarket.controller.request_forms.CalculatePriceForm;
import com.itacwt.phonemarket.controller.request_forms.PurchaseForm;
import com.itacwt.phonemarket.controller.response_forms.ResponseForm;
import com.itacwt.phonemarket.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @PostMapping(value = "/calculate-price")
    public ResponseForm calculatePrice(@Valid @RequestBody final CalculatePriceForm calculatePriceForm) throws Exception {
        return ResponseForm.successWithResult(
                purchaseService.calculatePrice(calculatePriceForm)
        );
    }

    @PostMapping(value = "/purchase")
    public ResponseForm purchase(@Valid @RequestBody final PurchaseForm purchaseForm) throws Exception {
        return ResponseForm.successWithResult(
                purchaseService.processPurchase(purchaseForm)
        );
    }
}
