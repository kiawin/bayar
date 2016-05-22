package com.senang.bayar.core.provider;

import com.senang.bayar.core.invoice.Invoice.Type;

import java.util.Map;

/**
 * Payment provider
 */
public abstract class PaymentProvider implements
        HasType<Type>,
        HasFee<Float> {

    Type type;
    String name;

    Float fee;
    Map<String, Float> fees;

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String getTypeName() {
        return getType().toString().toLowerCase();
    }

    @Override
    public String getDefaultTypeName() {
        return Type.DEFAULT.toString().toLowerCase();
    }

    @Override
    public Float getFee() {
        if (getFees().containsKey(getTypeName())) {
            return getFees().get(getTypeName());
        } else {
            return getFees().get(getDefaultTypeName());
        }
    }

    @Override
    public void setFee(Float fee) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Float getRate() {
        return getFees().get(getTypeName())/100;
    }

    @Override
    public Map<String, Float> getFees() {
        return this.fees;
    }

    @Override
    public void setFees(Map<String, Float> fees) {
        this.fees = fees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
