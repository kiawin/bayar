package com.senang.bayar.core.provider;

import com.senang.bayar.core.invoice.Invoice.Type;

/**
 * Direct debit payment provider - Generic
 */
public class GenericDirectDebitPaymentProvider extends DirectDebitPaymentProvider {

    public static final String NAME = "Generic";

    public GenericDirectDebitPaymentProvider(Type type) {
        setName(NAME);
        setType(type);
    }
}
