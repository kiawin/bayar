package com.senang.bayar.core.provider;

import com.neovisionaries.i18n.CountryCode;
import java.util.Map;
import com.senang.bayar.core.invoice.Invoice.Type;

/**
 * Credit card payment provider - Generic
 */
public class GenericCreditCardPaymentProvider extends CreditCardPaymentProvider {

    public static final String NAME = "Generic";

    public GenericCreditCardPaymentProvider(Type type, CountryCode country, Map<String, Float> fees) {
        setName(NAME);
        setType(type);
        setCountry(country);
        setFees(fees);
    }
}
