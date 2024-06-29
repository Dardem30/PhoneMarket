package com.itacwt.phonemarket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itacwt.phonemarket.beans.enums.PaymentProcessorType;
import com.itacwt.phonemarket.controller.forms.CalculatePriceForm;
import com.itacwt.phonemarket.controller.forms.PurchaseForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PhoneMarketApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCalculatePriceValidRequest() throws Exception {
        CalculatePriceForm form = new CalculatePriceForm();
        form.setProduct(1L);
        form.setTaxNumber("IT123456789");
        form.setCouponCode("P10");

        mockMvc.perform(post("/calculate-price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result").value(109.8))
                .andDo(print());
    }

    @Test
    public void testCalculatePriceMissingProduct() throws Exception {
        final Long missingProductId = 50L;
        final CalculatePriceForm form = new CalculatePriceForm();
        form.setProduct(missingProductId);
        form.setTaxNumber("RU123454235");
        form.setCouponCode("P10");

        mockMvc.perform(post("/calculate-price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(String.format("No product with id [%d]", missingProductId)))
                .andDo(print());
    }

    @Test
    public void testCalculatePriceNoCountryConfigured() throws Exception {
        final String invalidCountryPrefix = "RU";
        final CalculatePriceForm form = new CalculatePriceForm();
        form.setProduct(1L);
        form.setTaxNumber(invalidCountryPrefix + "123454235");
        form.setCouponCode("P10");

        mockMvc.perform(post("/calculate-price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(String.format("No tax settings configured for [%s]", invalidCountryPrefix)))
                .andDo(print());
    }

    @Test
    public void testCalculatePriceInvalidTaxNumber() throws Exception {
        final String invalidTaxNumber = "IT123";
        final CalculatePriceForm form = new CalculatePriceForm();
        form.setProduct(1L);
        form.setTaxNumber(invalidTaxNumber);
        form.setCouponCode("P10");

        mockMvc.perform(post("/calculate-price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(String.format("Wrong taxNumber [%s]", invalidTaxNumber)))
                .andDo(print());
    }

    @Test
    public void testCalculatePriceWrongCoupon() throws Exception {
        final String missingCouponName = "qwe";
        final CalculatePriceForm form = new CalculatePriceForm();
        form.setProduct(1L);
        form.setTaxNumber("IT123456789");
        form.setCouponCode(missingCouponName);

        mockMvc.perform(post("/calculate-price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(String.format("No coupon with code [%s]", missingCouponName)))
                .andDo(print());
    }

    @Test
    public void testCalculatePriceNoProduct() throws Exception {
        CalculatePriceForm form = new CalculatePriceForm();
        form.setTaxNumber("RU123454235");
        form.setCouponCode("P10");

        mockMvc.perform(post("/calculate-price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andDo(print());
    }

    @Test
    public void testPurchasePaypalValidRequest() throws Exception {
        final PurchaseForm form = new PurchaseForm();
        form.setProduct(1L);
        form.setTaxNumber("IT123456789");
        form.setCouponCode("P10");
        form.setPaymentProcessor(PaymentProcessorType.paypal.name());

        mockMvc.perform(post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(print());
    }

    @Test
    public void testPurchaseStripeValidRequest() throws Exception {
        final PurchaseForm form = new PurchaseForm();
        form.setProduct(1L);
        form.setTaxNumber("IT123456789");
        form.setCouponCode("P10");
        form.setPaymentProcessor(PaymentProcessorType.stripe.name());

        mockMvc.perform(post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(print());
    }

    @Test
    public void testPurchaseInvalidPaymentProcessor() throws Exception {
        final String invalidPaymentProcessor = "test";
        final PurchaseForm form = new PurchaseForm();
        form.setProduct(1L);
        form.setTaxNumber("IT123456789");
        form.setCouponCode("P10");
        form.setPaymentProcessor(invalidPaymentProcessor);

        mockMvc.perform(post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(String.format("No payment processor such as [%s]", invalidPaymentProcessor)))
                .andDo(print());
    }

    @Test
    public void testPurchaseStripeTooCheapToBePaid() throws Exception {
        final Long cheapProductId = 2L;
        final PurchaseForm form = new PurchaseForm();
        form.setProduct(cheapProductId);
        form.setTaxNumber("IT123456789");
        form.setCouponCode("P10");
        form.setPaymentProcessor(PaymentProcessorType.stripe.name());

        mockMvc.perform(post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result.purchaseStatus").value(false))
                .andDo(print());
    }
    @Test
    public void testPurchasePayPalWithOvercamePriceLimit() throws Exception {
        final Long tooExpensiveProductId = 4L;
        final PurchaseForm form = new PurchaseForm();
        form.setProduct(tooExpensiveProductId);
        form.setTaxNumber("IT123456789");
        form.setCouponCode("P10");
        form.setPaymentProcessor(PaymentProcessorType.paypal.name());

        mockMvc.perform(post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Price can't be more than 100000"))
                .andDo(print());
    }

}
