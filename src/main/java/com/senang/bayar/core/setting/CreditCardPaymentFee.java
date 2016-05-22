package com.senang.bayar.core.setting;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Provider payment fee - Credit Card
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="paymentProviderFees.credit")
public class CreditCardPaymentFee extends PaymentFee {

    Map<String, Float> paypal;
    Map<String, Float> maybank;
    Map<String, Float> hsbc;

    public CreditCardPaymentFee() {

    }

    public CreditCardPaymentFee(Map<String, Float> paypal, Map<String, Float> maybank, Map<String, Float> hsbc, Map<String, Float> generic) {
        this.paypal = paypal;
        this.maybank = maybank;
        this.hsbc = hsbc;
        this.generic = generic;
    }

    public Map<String, Float> getPaypal() {
        return paypal;
    }

    public void setPaypal(Map<String, Float> paypal) {
        this.paypal = paypal;
    }

    public Map<String, Float> getMaybank() {
        return maybank;
    }

    public void setMaybank(Map<String, Float> maybank) {
        this.maybank = maybank;
    }

    public Map<String, Float> getHsbc() {
        return hsbc;
    }

    public void setHsbc(Map<String, Float> hsbc) {
        this.hsbc = hsbc;
    }
}