package com.senang.bayar.core.provider;

import com.neovisionaries.i18n.CountryCode;

/**
 * Payment Provider - Credit Card
 */
public abstract class CreditCardPaymentProvider extends PaymentProvider implements
        HasCountry<CountryCode> {

    CountryCode country;

    @Override
    public String getTypeName() {
        if (getFees().containsKey(super.getTypeName())) {
            return super.getTypeName();
        } else {
            return super.getDefaultTypeName();
        }
    }

    @Override
    public CountryCode getCountry() {
        return this.country;
    }

    @Override
    public void setCountry(CountryCode country) {
        this.country = country;
    }
}
