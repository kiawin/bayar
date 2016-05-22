package com.senang.bayar;

import com.senang.bayar.core.invoice.Invoice;
import com.senang.bayar.core.setting.CreditCardPaymentFee;
import com.senang.bayar.core.setting.DirectDebitPaymentFee;
import com.senang.bayar.service.InvoiceService;
import com.senang.bayar.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * RoutingApplication
 */
@SpringBootApplication
public class RoutingApplication implements CommandLineRunner {

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    RuleService ruleService;

    @Autowired
    CreditCardPaymentFee creditCardPaymentFee;

    @Autowired
    DirectDebitPaymentFee directDebitPaymentFee;

    public static void main(String[] args) {
        SpringApplication.run(RoutingApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Invoice invoice = invoiceService.create();

        if (invoice.getMethod().equals(Invoice.Method.DIRECT)) {
            ruleService.enable(invoice, directDebitPaymentFee);
        } else {
            ruleService.enable(invoice, creditCardPaymentFee);
        }
    }
}