package com.senang.bayar.core.rules;

import org.easyrules.annotation.Action;

/**
 * Rule: Credit Card
 */
public abstract class CreditCardRule extends BaseRule {

    @Override
    @Action
    public void then() {
        System.out.println("Rule: " + getRuleName());
        System.out.println("Amount: " + getInvoice().getAmount());
        System.out.println("Processing Fee: " + getFee());
        System.out.println("Total: " + getInvoice().getAmount().add(getFee()));
    }
}