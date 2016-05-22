package com.senang.bayar.core.provider;

/**
 * Payment Provider - Direct Debit
 */
public abstract class DirectDebitPaymentProvider extends PaymentProvider {

    @Override
    public String getTypeName() {
        if (getFees().containsKey(super.getTypeName())) {
            return super.getTypeName();
        } else {
            return super.getDefaultTypeName();
        }
    }
}
