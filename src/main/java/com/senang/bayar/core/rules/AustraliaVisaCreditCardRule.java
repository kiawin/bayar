package com.senang.bayar.core.rules;

import com.neovisionaries.i18n.CountryCode;
import com.senang.bayar.core.invoice.Invoice;
import com.senang.bayar.core.provider.MaybankCreditCardPaymentProvider;
import com.senang.bayar.core.provider.PaymentProvider;
import com.senang.bayar.core.provider.PaypalCreditCardPaymentProvider;
import com.senang.bayar.core.setting.CreditCardPaymentFee;
import com.senang.bayar.core.setting.PaymentFee;
import org.easyrules.annotation.Condition;
import org.easyrules.spring.SpringRule;

/**
 * Rule: Amount above $1000, use PayPal
 */
@SpringRule
public class AustraliaVisaCreditCardRule extends CreditCardRule {

    private static final String RULE_NAME = "Australia VISA";
    private static final int PRIORITY = 1;

    public AustraliaVisaCreditCardRule(Invoice invoice, PaymentFee paymentFee) {
        setInvoice(invoice);
        setPaymentFee(paymentFee);
        setRuleName(RULE_NAME);
        setPriority(PRIORITY);
    }

    @Override
    @Condition
    public boolean when() {
        // if not Australia VISA Credit Card, exit
        if (!getInvoice().getMethod().equals(Invoice.Method.CREDIT)
                || !getInvoice().getType().equals(Invoice.Type.VISA)
                || !getInvoice().getCountry().equals(CountryCode.AU)
                ) {
            return false;
        }

        String visa = Invoice.Type.VISA.toString().toLowerCase();
        float maybankFee = ((CreditCardPaymentFee) getPaymentFee()).getMaybank().get(visa);
        float paypalFee = ((CreditCardPaymentFee) getPaymentFee()).getPaypal().get(visa);

        // Choose cheaper rate between Paypal and Maybank
        PaymentProvider paymentProvider;
        if (paypalFee <= maybankFee) {
            paymentProvider = new PaypalCreditCardPaymentProvider(
                    getInvoice().getType(),
                    getInvoice().getCountry(),
                    ((CreditCardPaymentFee) getPaymentFee()).getPaypal()
            );
        } else {
            paymentProvider = new MaybankCreditCardPaymentProvider(
                    getInvoice().getType(),
                    getInvoice().getCountry(),
                    ((CreditCardPaymentFee) getPaymentFee()).getMaybank()
            );
        }
        updateInvoice(getInvoice(), paymentProvider);

        float rate = getInvoice().getPaymentProvider().getRate();
        setFee(getInvoice().getAmount().multiply(rate));

        return true;
    }
}
