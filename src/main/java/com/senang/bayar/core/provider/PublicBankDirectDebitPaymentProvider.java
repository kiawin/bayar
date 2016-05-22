package com.senang.bayar.core.provider;

import com.senang.bayar.core.invoice.Invoice.Type;

/**
 * Direct debit payment provider - Public Bank
 */
public class PublicBankDirectDebitPaymentProvider extends DirectDebitPaymentProvider {

    public static final String NAME = "PublicBank";

    public PublicBankDirectDebitPaymentProvider(Type type) {
        setName(NAME);
        setType(type);
    }
}