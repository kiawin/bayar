package com.senang.bayar.core.rules;

import com.senang.bayar.core.invoice.Invoice;
import com.senang.bayar.core.provider.DbsDirectDebitPaymentProvider;
import com.senang.bayar.core.provider.PaymentProvider;
import com.senang.bayar.core.provider.PublicBankDirectDebitPaymentProvider;
import com.senang.bayar.core.setting.PaymentFee;
import org.easyrules.annotation.Condition;
import org.easyrules.spring.SpringRule;
import org.javamoney.moneta.FastMoney;

/**
 * Direct Debit Rule: Prioritize local bank
 */
@SpringRule
public class PrioritizeLocalBankDirectDebitRule extends DirectDebitRule {

    private static final String RULE_NAME = "Direct Debit Rule - Prioritize Local Bank";
    private static final int PRIORITY = 10;

    public PrioritizeLocalBankDirectDebitRule(Invoice invoice, PaymentFee paymentFee) {
        setInvoice(invoice);
        setPaymentFee(paymentFee);
        setRuleName(RULE_NAME);
        setPriority(PRIORITY);
    }

    @Override
    @Condition
    public boolean when() {
        if (!getInvoice().getMethod().equals(Invoice.Method.DIRECT)) {
            return false;
        }

        // Determine payment provider based on country
        // If no predetermined country, fall back to PublicBank
        PaymentProvider paymentProvider;
        switch(getInvoice().getCountry()) {
            case MY:
                paymentProvider = new PublicBankDirectDebitPaymentProvider(
                        getInvoice().getType()
                );
                updateInvoice(getInvoice(), paymentProvider);

                setFee(FastMoney.of(0, getInvoice().getCountry().getCurrency().getCurrencyCode()));

                return true;
            case SG:
                paymentProvider = new DbsDirectDebitPaymentProvider(
                        getInvoice().getType()
                );
                updateInvoice(getInvoice(), paymentProvider);

                setFee(FastMoney.of(0, getInvoice().getCountry().getCurrency().getCurrencyCode()));

                return true;
            default:
                paymentProvider = new PublicBankDirectDebitPaymentProvider(
                        getInvoice().getType()
                );
                updateInvoice(getInvoice(), paymentProvider);

                setFee(FastMoney.of(0, getInvoice().getCountry().getCurrency().getCurrencyCode()));

                return true;
        }
    }
}
