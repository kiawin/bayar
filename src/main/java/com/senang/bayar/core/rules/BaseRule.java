package com.senang.bayar.core.rules;

import com.senang.bayar.core.invoice.Invoice;
import com.senang.bayar.core.provider.PaymentProvider;
import com.senang.bayar.core.setting.PaymentFee;
import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;
import org.easyrules.annotation.Priority;

import javax.money.MonetaryAmount;

/**
 * Rule: abstract rule
 */
public abstract class BaseRule {

    String ruleName;
    Invoice invoice;
    PaymentFee paymentFee;
    MonetaryAmount fee;
    int priority;

    @Condition
    public boolean when() {
        return true;
    }

    @Action
    public void then() {
        return;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public PaymentFee getPaymentFee() {
        return paymentFee;
    }

    public void setPaymentFee(PaymentFee paymentFee) {
        this.paymentFee = paymentFee;
    }

    public MonetaryAmount getFee() {
        return fee;
    }

    public void setFee(MonetaryAmount fee) {
        this.fee = fee;
    }

    @Priority
    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    protected void updateInvoice(Invoice invoice, PaymentProvider paymentProvider) {
        this.invoice.setPaymentProvider(paymentProvider);
        this.invoice.setBank(paymentProvider.getName());
    }
}