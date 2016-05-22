package com.senang.bayar.core.provider;

import com.neovisionaries.i18n.CountryCode;
import java.util.Map;
import com.senang.bayar.core.invoice.Invoice.Type;

/**
 * Credit card payment provider - HSBC
 */
public class HsbcCreditCardPaymentProvider extends CreditCardPaymentProvider {

    public static final String NAME = "Hsbc";

    public HsbcCreditCardPaymentProvider(Type type, CountryCode country, Map<String, Float> fees) {
        setName(NAME);
        setType(type);
        setCountry(country);
        setFees(fees);
    }

}