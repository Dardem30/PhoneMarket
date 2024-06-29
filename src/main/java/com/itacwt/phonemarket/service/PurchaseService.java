package com.itacwt.phonemarket.service;

import com.itacwt.phonemarket.beans.exceptions.ResponseValidationException;
import com.itacwt.phonemarket.controller.forms.CalculatePriceForm;
import com.itacwt.phonemarket.entity.Country;
import com.itacwt.phonemarket.entity.Coupon;
import com.itacwt.phonemarket.entity.Product;
import com.itacwt.phonemarket.repository.CountryRepository;
import com.itacwt.phonemarket.repository.CouponRepository;
import com.itacwt.phonemarket.repository.ProductRepository;
import com.itacwt.phonemarket.utils.BigDecimalUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class PurchaseService {
    private final CouponRepository couponRepository;
    private final CountryRepository countryRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public BigDecimal calculatePrice(final CalculatePriceForm calculatePriceForm) throws Exception {
        final Product product = productRepository.findById(calculatePriceForm.getProduct())
                .orElseThrow(() -> new ResponseValidationException(
                        String.format("No product with id [%d]", calculatePriceForm.getProduct())
                ));

        final String taxCountryPrefix = calculatePriceForm.getTaxNumber().substring(0, 2);
        final Country country = countryRepository.findByPrefix(taxCountryPrefix);
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
}
