package com.senang.bayar.core.provider;

import com.senang.bayar.core.invoice.Invoice.Type;

/**
 * Direct debit payment provider - Dbs
 */
public class DbsDirectDebitPaymentProvider extends DirectDebitPaymentProvider {

    public static final String NAME = "Dbs";

    public DbsDirectDebitPaymentProvider(Type type) {
        setName(NAME);
        setType(type);
    }
}