package com.senang.bayar.service;

import com.neovisionaries.i18n.CountryCode;
import com.senang.bayar.core.invoice.Invoice;
import com.senang.bayar.core.setting.CreditCardPaymentFee;
import com.senang.bayar.core.setting.DirectDebitPaymentFee;
import org.javamoney.moneta.FastMoney;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.test.OutputCapture;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test - Invoice Service
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class RuleServiceTest {

    @org.springframework.context.annotation.Configuration
    static class Configuration {
        static InvoiceService invoiceService = mock(InvoiceService.class);
        static CreditCardPaymentFee creditCardPaymentFee = mock(CreditCardPaymentFee.class);
        static DirectDebitPaymentFee directDebitPaymentFee = mock(DirectDebitPaymentFee.class);

        @Bean
        InvoiceService invoiceService() {
            return invoiceService;
        }

        @Bean
        RuleService ruleService() {
            return new RuleService();
        }

        @Bean
        CreditCardPaymentFee creditCardPaymentFee() {
            return creditCardPaymentFee;
        }

        @Bean
        DirectDebitPaymentFee directDebitPaymentFee() {
            return directDebitPaymentFee;
        }

        @Bean
        static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
            PropertyPlaceholderConfigurer propertyPlaceholderConfigurer =  new PropertyPlaceholderConfigurer();
            propertyPlaceholderConfigurer.setLocation(new ClassPathResource("args.properties"));
            return propertyPlaceholderConfigurer;
        }
    }

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    RuleService ruleService;

    @Autowired
    CreditCardPaymentFee creditCardPaymentFee;

    @Autowired
    DirectDebitPaymentFee directDebitPaymentFee;

    @Rule
    public OutputCapture capture = new OutputCapture();

    @Before
    public void setUp() {
        when(creditCardPaymentFee.getMaybank()).thenReturn(new HashMap<String, Float>() {{
            put("default", 33.1f);
            put("amex", 33.2f);
            put("visa", 33.3f);
        }});
        when(creditCardPaymentFee.getPaypal()).thenReturn(new HashMap<String, Float>() {{
            put("default", 22.1f);
            put("visa", 22.2f);
        }});
        when(creditCardPaymentFee.getGeneric()).thenReturn(new HashMap<String, Float>() {{
            put("default", 55.1f);
        }});

        when(directDebitPaymentFee.getGeneric()).thenReturn(new HashMap<String, Float>() {{
            put("default", 0f);
        }});
    }

    @Test
    public void enableCreditCardAmountAboveOneThousand() {
        Invoice invoice = new Invoice();
        invoice.setFrom("John");
        invoice.setTo("Doe");
        invoice.setMethod(Invoice.Method.CREDIT);
        invoice.setType(Invoice.Type.MASTER);
        invoice.setAmount(FastMoney.of(1001, "MYR"));
        invoice.setCountry(CountryCode.valueOf("MY"));

        ruleService.enable(invoice, creditCardPaymentFee);

        assertThat(invoice.getPaymentProvider().getName(), is("Paypal"));
        assertThat(capture.toString(), containsString("Rule: Amount above $1000 rule"));
        assertThat(capture.toString(), containsString("Amount: MYR 1001.00000"));
        assertThat(capture.toString(), containsString("Processing Fee: MYR 221.22100"));
        assertThat(capture.toString(), containsString("Total: MYR 1222.22100"));
    }

    @Test
    public void enableCreditCardDefault() {
        Invoice invoice = new Invoice();
        invoice.setFrom("John");
        invoice.setTo("Doe");
        invoice.setMethod(Invoice.Method.CREDIT);
        invoice.setType(Invoice.Type.DEFAULT);
        invoice.setAmount(FastMoney.of(100, "MYR"));
        invoice.setCountry(CountryCode.valueOf("MY"));

        ruleService.enable(invoice, creditCardPaymentFee);

        assertThat(invoice.getPaymentProvider().getName(), is("Generic"));
        assertThat(capture.toString(), containsString("Rule: Credit Card - Default rule"));
        assertThat(capture.toString(), containsString("Amount: MYR 100.00000"));
        assertThat(capture.toString(), containsString("Processing Fee: MYR 55.10000"));
        assertThat(capture.toString(), containsString("Total: MYR 155.10000"));
    }

    @Test
    public void enableDirectDebitDefault() {
        Invoice invoice = new Invoice();
        invoice.setFrom("John");
        invoice.setTo("Doe");
        invoice.setMethod(Invoice.Method.DIRECT);
        invoice.setType(Invoice.Type.DEFAULT);
        invoice.setAmount(FastMoney.of(100, "MYR"));
        invoice.setCountry(CountryCode.valueOf("MY"));

        ruleService.enable(invoice, directDebitPaymentFee);

        // NOTE: Instead of triggering default rule,
        // local bank direct debit rule will be loaded
        assertThat(invoice.getPaymentProvider().getName(), not("Generic"));
        assertFalse(capture.toString().contains("Rule: Direct Debit - Default rule"));

        assertThat(invoice.getPaymentProvider().getName(), is("PublicBank"));
        assertThat(capture.toString(), containsString("Rule: Direct Debit Rule - Prioritize Local Bank"));
        assertThat(capture.toString(), containsString("Bank: PublicBank"));
        assertThat(capture.toString(), containsString("Amount: MYR 100.00000"));
        assertThat(capture.toString(), containsString("Processing Fee: MYR 0.00000"));
        assertThat(capture.toString(), containsString("Total: MYR 100.00000"));
    }

    @Test
    public void enableDirectDebitPrioritizeLocalBankMalaysia() {
        Invoice invoice = new Invoice();
        invoice.setFrom("John");
        invoice.setTo("Doe");
        invoice.setMethod(Invoice.Method.DIRECT);
        invoice.setType(Invoice.Type.DEFAULT);
        invoice.setAmount(FastMoney.of(100, "MYR"));
        invoice.setCountry(CountryCode.valueOf("MY"));

        ruleService.enable(invoice, directDebitPaymentFee);

        assertThat(invoice.getPaymentProvider().getName(), is("PublicBank"));
        assertThat(capture.toString(), containsString("Rule: Direct Debit Rule - Prioritize Local Bank"));
        assertThat(capture.toString(), containsString("Bank: PublicBank"));
        assertThat(capture.toString(), containsString("Amount: MYR 100.00000"));
        assertThat(capture.toString(), containsString("Processing Fee: MYR 0.00000"));
        assertThat(capture.toString(), containsString("Total: MYR 100.00000"));
    }

    @Test
    public void enableDirectDebitPrioritizeLocalBankSingapore() {
        Invoice invoice = new Invoice();
        invoice.setFrom("John");
        invoice.setTo("Doe");
        invoice.setMethod(Invoice.Method.DIRECT);
        invoice.setType(Invoice.Type.DEFAULT);
        invoice.setAmount(FastMoney.of(100, "SGD"));
        invoice.setCountry(CountryCode.valueOf("SG"));

        ruleService.enable(invoice, directDebitPaymentFee);

        assertThat(invoice.getPaymentProvider().getName(), is("Dbs"));
        assertThat(capture.toString(), containsString("Rule: Direct Debit Rule - Prioritize Local Bank"));
        assertThat(capture.toString(), containsString("Bank: Dbs"));
        assertThat(capture.toString(), containsString("Amount: SGD 100.00000"));
        assertThat(capture.toString(), containsString("Processing Fee: SGD 0.00000"));
        assertThat(capture.toString(), containsString("Total: SGD 100.00000"));
    }

    @Test
    public void enableCreditCardMalaysiaAmex() {
        Invoice invoice = new Invoice();
        invoice.setFrom("John");
        invoice.setTo("Doe");
        invoice.setMethod(Invoice.Method.CREDIT);
        invoice.setType(Invoice.Type.AMEX);
        invoice.setAmount(FastMoney.of(1001, "MYR"));
        invoice.setCountry(CountryCode.valueOf("MY"));

        ruleService.enable(invoice, creditCardPaymentFee);

        // NOTE: Instead of triggering amount above $1000 rule,
        // Malaysia AMEX rule will be loaded
        assertThat(invoice.getPaymentProvider().getName(), not("Paypal"));
        assertFalse(capture.toString().contains("Rule: Amount above $1000 rule"));

        assertThat(invoice.getPaymentProvider().getName(), is("Maybank"));
        assertThat(capture.toString(), containsString("Rule: Malaysia AMEX"));
        assertThat(capture.toString(), containsString("Amount: MYR 1001.00000"));
        assertThat(capture.toString(), containsString("Processing Fee: MYR 332.33202"));
        assertThat(capture.toString(), containsString("Total: MYR 1333.33202"));
    }

    @Test
    public void enableCreditCardSingaporeAmex() {
        Invoice invoice = new Invoice();
        invoice.setFrom("John");
        invoice.setTo("Doe");
        invoice.setMethod(Invoice.Method.CREDIT);
        invoice.setType(Invoice.Type.AMEX);
        invoice.setAmount(FastMoney.of(1001, "SGD"));
        invoice.setCountry(CountryCode.valueOf("SG"));

        ruleService.enable(invoice, creditCardPaymentFee);

        // NOTE: Instead of triggering Malaysia AMEX,
        // Amount above $1000 rule will be loaded
        assertThat(invoice.getPaymentProvider().getName(), not("Maybank"));
        assertFalse(capture.toString().contains("Rule: Malaysia AMEX"));

        assertThat(invoice.getPaymentProvider().getName(), is("Paypal"));
        assertThat(capture.toString(), containsString("Rule: Amount above $1000 rule"));
        assertThat(capture.toString(), containsString("Amount: SGD 1001.00000"));
        assertThat(capture.toString(), containsString("Processing Fee: SGD 221.22100"));
        assertThat(capture.toString(), containsString("Total: SGD 1222.22100"));
    }

    @Test
    public void enableCreditCardAustraliaVisa() {
        Invoice invoice = new Invoice();
        invoice.setFrom("John");
        invoice.setTo("Doe");
        invoice.setMethod(Invoice.Method.CREDIT);
        invoice.setType(Invoice.Type.VISA);
        invoice.setAmount(FastMoney.of(1001, "AUD"));
        invoice.setCountry(CountryCode.valueOf("AU"));

        ruleService.enable(invoice, creditCardPaymentFee);

        // NOTE: Instead of using Maybank,
        // Paypal will be chosen as preferred provider
        assertThat(invoice.getPaymentProvider().getName(), not("Maybank"));

        assertThat(invoice.getPaymentProvider().getName(), is("Paypal"));
        assertThat(capture.toString(), containsString("Rule: Australia VISA"));
        assertThat(capture.toString(), containsString("Amount: AUD 1001.00000"));
        assertThat(capture.toString(), containsString("Processing Fee: AUD 222.22200"));
        assertThat(capture.toString(), containsString("Total: AUD 1223.22200"));
    }
}