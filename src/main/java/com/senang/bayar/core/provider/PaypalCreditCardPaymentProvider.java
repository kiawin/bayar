package com.senang.bayar.core.provider;

import com.neovisionaries.i18n.CountryCode;
import java.util.Map;
import com.senang.bayar.core.invoice.Invoice.Type;

/**
 * Credit card payment provider - PayPal
 */
public class PaypalCreditCardPaymentProvider extends CreditCardPaymentProvider {

    public static final String NAME = "Paypal";

    public PaypalCreditCardPaymentProvider(Type type, CountryCode country, Map<String, Float> fees) {
        setName(NAME);
        setType(type);
        setCountry(country);
        setFees(fees);
    }
}
