package com.senang.bayar.core.rules;

import com.senang.bayar.core.invoice.Invoice;
import com.senang.bayar.core.provider.PaymentProvider;
import com.senang.bayar.core.provider.PaypalCreditCardPaymentProvider;
import com.senang.bayar.core.setting.CreditCardPaymentFee;
import com.senang.bayar.core.setting.PaymentFee;
import org.easyrules.annotation.Condition;
import org.easyrules.spring.SpringRule;
import org.javamoney.moneta.FastMoney;

/**
 * Rule: Amount above $1000, use PayPal
 */
@SpringRule
public class AmountAboveOneThousandCreditCardRule extends CreditCardRule {

    private static final String RULE_NAME = "Amount above $1000 rule";
    private static final int PRIORITY = 10;

    public AmountAboveOneThousandCreditCardRule(Invoice invoice, PaymentFee paymentFee) {
        setInvoice(invoice);
        setPaymentFee(paymentFee);
        setRuleName(RULE_NAME);
        setPriority(PRIORITY);
    }

    @Override
    @Condition
    public boolean when() {
        if (!getInvoice().getMethod().equals(Invoice.Method.CREDIT)) {
            return false;
        }

        // If less than $1000 of any currency, exit
        if (getInvoice()
                .getAmount()
                .isLessThanOrEqualTo(FastMoney.of(1000, getInvoice().getCountry().getCurrency().getCurrencyCode()))
                ) {
            return false;
        }

        PaymentProvider paymentProvider = new PaypalCreditCardPaymentProvider(
                getInvoice().getType(),
                getInvoice().getCountry(),
                ((CreditCardPaymentFee) getPaymentFee()).getPaypal()
        );
        updateInvoice(getInvoice(), paymentProvider);

        float rate = getInvoice().getPaymentProvider().getRate();
        setFee(getInvoice().getAmount().multiply(rate));

        return true;
    }
}
