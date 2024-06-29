package com.itacwt.phonemarket.service.payment_processors;

import com.itacwt.phonemarket.beans.PurchaseRequest;
import com.itacwt.phonemarket.beans.exceptions.ResponseValidationException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service(value = "paypal_payment_processor")
public class Paypal implements PaymentProcessor {
    @Override
    public Map<String, Object> processPurchaseRequest(PurchaseRequest purchaseRequest) throws Exception {
        makePayment(purchaseRequest.getPrice().intValue());
        return null;
    }

    private void makePayment(final Integer price) throws Exception {
        if (price > 100000) {
            throw new ResponseValidationException("Price can't be more than 100000");
        }
    }
}
