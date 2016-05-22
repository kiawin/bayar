package com.senang.bayar.core.setting;

import java.util.Map;

/**
 * PaymentFee
 */
public abstract class PaymentFee {

    Map<String, Float> generic;

    public Map<String, Float> getGeneric() {
        return generic;
    }

    public void setGeneric(Map<String, Float> generic) {
        this.generic = generic;
    }
}