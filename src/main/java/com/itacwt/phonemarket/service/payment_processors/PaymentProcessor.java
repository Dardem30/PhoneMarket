package com.itacwt.phonemarket.service.payment_processors;

import com.itacwt.phonemarket.beans.PurchaseRequest;

import java.util.Map;

public interface PaymentProcessor {
    Map<String, Object> processPurchaseRequest(PurchaseRequest purchaseRequest) throws Exception;
}
