package com.senang.bayar.core.provider;

import com.neovisionaries.i18n.CountryCode;

/**
 * Interface: Provider Country
 */
public interface HasCountry<C extends CountryCode> {

    /**
     * Get CountryCode object
     *
     * @return
     */
    C getCountry();

    /**
     * Set CountryCode object
     *
     * @param country
     */
    void setCountry(C country);
}