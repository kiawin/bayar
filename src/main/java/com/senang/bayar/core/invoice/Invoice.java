package com.senang.bayar.core.invoice;

import com.neovisionaries.i18n.CountryCode;
import com.senang.bayar.core.provider.PaymentProvider;

import javax.money.MonetaryAmount;

/**
 * Entity: Invoice
 */
public class Invoice {

    public static enum Method {
        DIRECT,
        CREDIT
    }

    public static enum Type {
        DEFAULT,
        VISA,
        MASTER,
        AMEX
    }

    private String from;
    private String to;
    private Method method;
    private Type type;
    private MonetaryAmount amount;
    private CountryCode country;
    private String bank;
    private PaymentProvider paymentProvider;

    public Invoice() {
    }

    public Invoice(String from, String to, Method method, Type type, MonetaryAmount amount, CountryCode country) {
        this.from = from;
        this.to = to;
        this.method = method;
        this.type = type;
        this.amount = amount;
        this.country = country;
    }

    public Invoice(String from, String to, Method method, Type type, MonetaryAmount amount, CountryCode country, String bank) {
        this.from = from;
        this.to = to;
        this.method = method;
        this.type = type;
        this.amount = amount;
        this.country = country;
        this.bank = bank;
    }

    public Invoice(String from, String to, Method method, Type type, MonetaryAmount amount, CountryCode country, PaymentProvider paymentProvider) {
        this.from = from;
        this.to = to;
        this.method = method;
        this.type = type;
        this.amount = amount;
        this.country = country;
        this.paymentProvider = paymentProvider;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTypeName() {
        return this.type.toString().toLowerCase();
    }

    public String getDefaultTypeName() {
        return Type.DEFAULT.toString().toLowerCase();
    }

    public MonetaryAmount getAmount() {
        return amount;
    }

    public void setAmount(MonetaryAmount amount) {
        this.amount = amount;
    }

    public CountryCode getCountry() {
        return country;
    }

    public void setCountry(CountryCode country) {
        this.country = country;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public PaymentProvider getPaymentProvider() {
        return paymentProvider;
    }

    public void setPaymentProvider(PaymentProvider paymentProvider) {
        this.paymentProvider = paymentProvider;
    }
}
