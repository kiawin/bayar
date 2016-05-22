package com.senang.bayar.core.rules;

import com.senang.bayar.core.invoice.Invoice;
import com.senang.bayar.core.provider.GenericCreditCardPaymentProvider;
import com.senang.bayar.core.provider.PaymentProvider;
import com.senang.bayar.core.setting.CreditCardPaymentFee;
import com.senang.bayar.core.setting.PaymentFee;
import org.easyrules.annotation.Condition;
import org.easyrules.spring.SpringRule;

/**
 * Rule: Default Credit Card
 */
@SpringRule
public class DefaultCreditCardRule extends CreditCardRule {

    private static final String RULE_NAME = "Credit Card - Default rule";
    private static final int PRIORITY = 99999;

    public DefaultCreditCardRule(Invoice invoice, PaymentFee paymentFee) {
        setInvoice(invoice);
        setPaymentFee(paymentFee);
        setRuleName(RULE_NAME);
        setPriority(PRIORITY);
    }

    @Override
    @Condition
    public boolean when() {
        if (getInvoice().getMethod().equals(Invoice.Method.CREDIT)) {
            PaymentProvider paymentProvider = new GenericCreditCardPaymentProvider(
                    getInvoice().getType(),
                    getInvoice().getCountry(),
                    getPaymentFee().getGeneric()
            );
            updateInvoice(getInvoice(), paymentProvider);

            float rate = ((CreditCardPaymentFee) getPaymentFee()).getGeneric().get(getInvoice().getPaymentProvider().getDefaultTypeName())/100;
            setFee(getInvoice().getAmount().multiply(rate));

            return true;
        } else {
            return false;
        }
    }
}