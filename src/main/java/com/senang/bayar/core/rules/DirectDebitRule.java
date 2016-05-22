package com.senang.bayar.core.rules;

import org.easyrules.annotation.Action;

/**
 * Rule: Direct Debit
 */
public abstract class DirectDebitRule extends BaseRule {

    @Override
    @Action
    public void then() {
        System.out.println("Rule: " + getRuleName());
        System.out.println("Bank: " + getInvoice().getBank());
        System.out.println("Amount: " + getInvoice().getAmount());
        System.out.println("Processing Fee: " + getFee());
        System.out.println("Total: " + getInvoice().getAmount().add(getFee()));
    }
}
