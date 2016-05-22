package com.senang.bayar.core.rules;

import com.neovisionaries.i18n.CountryCode;
import com.senang.bayar.core.invoice.Invoice;
import com.senang.bayar.core.provider.MaybankCreditCardPaymentProvider;
import com.senang.bayar.core.provider.PaymentProvider;
import com.senang.bayar.core.setting.CreditCardPaymentFee;
import com.senang.bayar.core.setting.PaymentFee;
import org.easyrules.annotation.Condition;
import org.easyrules.spring.SpringRule;

/**
 * Rule: Amount above $1000, use PayPal
 */
@SpringRule
public class MalaysiaAmexCreditCardRule extends CreditCardRule {

    private static final String RULE_NAME = "Malaysia AMEX";
    private static final int PRIORITY = 1;

    public MalaysiaAmexCreditCardRule(Invoice invoice, PaymentFee paymentFee) {
        setInvoice(invoice);
        setPaymentFee(paymentFee);
        setRuleName(RULE_NAME);
        setPriority(PRIORITY);
    }

    @Override
    @Condition
    public boolean when() {
        // If not Malaysia AMEX Credit Card, exit
        if (!getInvoice().getMethod().equals(Invoice.Method.CREDIT)
                || !getInvoice().getType().equals(Invoice.Type.AMEX)
                || !getInvoice().getCountry().equals(CountryCode.MY)
                ) {
            return false;
        }

        PaymentProvider paymentProvider = new MaybankCreditCardPaymentProvider(
                getInvoice().getType(),
                getInvoice().getCountry(),
                ((CreditCardPaymentFee) getPaymentFee()).getMaybank()
        );
        updateInvoice(getInvoice(), paymentProvider);

        float rate = getInvoice().getPaymentProvider().getRate();
        setFee(getInvoice().getAmount().multiply(rate));

        return true;
    }
}