package com.senang.bayar.core.rules;

import com.senang.bayar.core.invoice.Invoice;
import com.senang.bayar.core.provider.GenericDirectDebitPaymentProvider;
import com.senang.bayar.core.provider.PaymentProvider;
import com.senang.bayar.core.setting.DirectDebitPaymentFee;
import com.senang.bayar.core.setting.PaymentFee;
import org.easyrules.annotation.Condition;
import org.easyrules.spring.SpringRule;

/**
 * Rule: Amount above $1000, use PayPal
 */
@SpringRule
public class DefaultDirectDebitRule extends DirectDebitRule {

    private static final String RULE_NAME = "Direct Debit - Default rule";
    private static final int PRIORITY = 99999;

    public DefaultDirectDebitRule(Invoice invoice, PaymentFee paymentFee) {
        setInvoice(invoice);
        setPaymentFee(paymentFee);
        setRuleName(RULE_NAME);
        setPriority(PRIORITY);
    }

    @Override
    @Condition
    public boolean when() {
        if (getInvoice().getMethod().equals(Invoice.Method.DIRECT)) {
            PaymentProvider paymentProvider = new GenericDirectDebitPaymentProvider(
                    getInvoice().getType()
            );
            updateInvoice(getInvoice(), paymentProvider);

            float rate = getInvoice().getPaymentProvider().getRate();
            setFee(getInvoice().getAmount().multiply(rate));

            return true;
        } else {
            return false;
        }
    }
}
