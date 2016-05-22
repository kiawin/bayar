package com.senang.bayar.service;

import com.senang.bayar.core.invoice.Invoice;
import com.senang.bayar.core.rules.*;
import com.senang.bayar.core.setting.PaymentFee;
import org.easyrules.api.RulesEngine;
import org.springframework.stereotype.Component;

import static org.easyrules.core.RulesEngineBuilder.aNewRulesEngine;

/**
 * Rule Service
 */
@Component
public class RuleService {

    public void enable(Invoice invoice, PaymentFee paymentFee) {
        RulesEngine rulesEngine = aNewRulesEngine()
                .named("Payment Routing Engine")
                .withSkipOnFirstAppliedRule(true)
                .withSilentMode(true)
                .build();

        rulesEngine.registerRule(new AmountAboveOneThousandCreditCardRule(invoice, paymentFee));
        rulesEngine.registerRule(new MalaysiaAmexCreditCardRule(invoice, paymentFee));
        rulesEngine.registerRule(new AustraliaVisaCreditCardRule(invoice, paymentFee));
        rulesEngine.registerRule(new DefaultCreditCardRule(invoice, paymentFee));

        rulesEngine.registerRule(new PrioritizeLocalBankDirectDebitRule(invoice, paymentFee));
        rulesEngine.registerRule(new DefaultDirectDebitRule(invoice, paymentFee));

        rulesEngine.fireRules();
    }
}
