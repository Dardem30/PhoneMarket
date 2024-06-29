package com.itacwt.phonemarket.service;

import com.itacwt.phonemarket.beans.PurchaseRequest;
import com.itacwt.phonemarket.beans.enums.PaymentProcessorType;
import com.itacwt.phonemarket.beans.exceptions.ResponseValidationException;
import com.itacwt.phonemarket.controller.forms.CalculatePriceForm;
import com.itacwt.phonemarket.controller.forms.PurchaseForm;
import com.itacwt.phonemarket.entity.Country;
import com.itacwt.phonemarket.entity.Coupon;
import com.itacwt.phonemarket.entity.Product;
import com.itacwt.phonemarket.repository.CountryRepository;
import com.itacwt.phonemarket.repository.CouponRepository;
import com.itacwt.phonemarket.repository.ProductRepository;
import com.itacwt.phonemarket.service.payment_processors.PaymentProcessor;
import com.itacwt.phonemarket.utils.BigDecimalUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class PurchaseService {
    private final CouponRepository couponRepository;
    private final CountryRepository countryRepository;
    private final ProductRepository productRepository;
    private final PaymentProcessor payPalPaymentProcessor;
    private final PaymentProcessor stripePaymentProcessor;

    public PurchaseService(CouponRepository couponRepository,
                           CountryRepository countryRepository,
                           ProductRepository productRepository,
                           @Qualifier(value = "paypal_payment_processor") PaymentProcessor payPalPaymentProcessor,
                           @Qualifier(value = "stripe_payment_processor") PaymentProcessor stripePaymentProcessor
    ) {
        this.couponRepository = couponRepository;
        this.countryRepository = countryRepository;
        this.productRepository = productRepository;
        this.payPalPaymentProcessor = payPalPaymentProcessor;
        this.stripePaymentProcessor = stripePaymentProcessor;
    }

    @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
    public BigDecimal calculatePrice(final CalculatePriceForm calculatePriceForm) throws Exception {
        final Product product = productRepository.findById(calculatePriceForm.getProduct())
                .orElseThrow(() -> new ResponseValidationException(
                        String.format("No product with id [%d]", calculatePriceForm.getProduct())
                ));

        final String taxCountryPrefix = calculatePriceForm.getTaxNumber().substring(0, 2);
        final Country country = countryRepository.findByPrefix(taxCountryPrefix)
                .orElseThrow(() -> new ResponseValidationException(
                        String.format("No tax settings configured for [%s]", taxCountryPrefix)
                ));
        final Pattern countryTaxNumberPattern = Pattern.compile(country.getValidationRegex(), Pattern.CASE_INSENSITIVE);
        if (!countryTaxNumberPattern.matcher(calculatePriceForm.getTaxNumber().trim()).find()) {
            throw new ResponseValidationException(String.format("Wrong taxNumber [%s]", calculatePriceForm.getTaxNumber()));
        }
        BigDecimal price = product.getPrice();
        if (calculatePriceForm.getCouponCode() != null) {
            final Coupon coupon = couponRepository.findByCode(calculatePriceForm.getCouponCode())
                    .orElseThrow(() -> new ResponseValidationException(
                            String.format("No coupon with code [%s]", calculatePriceForm.getCouponCode())
                    ));
            price = coupon.apply(price);
        }
        price = BigDecimalUtils.addPercentage(price, country.getTaxRate());
        return price;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Map<String, Object> processPurchase(final PurchaseForm purchaseForm) throws Exception {
        final Map<String, Object> result;
        final BigDecimal price = calculatePrice(purchaseForm);
        final PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setPrice(price);
        if (PaymentProcessorType.paypal.name().equals(purchaseForm.getPaymentProcessor())) {
            result = payPalPaymentProcessor.processPurchaseRequest(purchaseRequest);
        } else if (PaymentProcessorType.stripe.name().equals(purchaseForm.getPaymentProcessor())) {
            result = stripePaymentProcessor.processPurchaseRequest(purchaseRequest);
        } else {
            throw new ResponseValidationException(String.format("No payment processor such as [%s]", purchaseForm.getPaymentProcessor()));
        }
        return result;
    }
}
