package com.itacwt.phonemarket.service.payment_processors;

import com.itacwt.phonemarket.beans.PurchaseRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service(value = "stripe_payment_processor")
public class Stripe implements PaymentProcessor {
    @Override
    public Map<String, Object> processPurchaseRequest(PurchaseRequest purchaseRequest) {
        return Collections.singletonMap("purchaseStatus", pay(purchaseRequest.getPrice().floatValue()));
    }
    private boolean pay(final Float price) {
        return price >= 100;
    }
}
