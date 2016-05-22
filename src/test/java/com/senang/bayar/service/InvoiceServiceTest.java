package com.senang.bayar.service;

import com.senang.bayar.core.invoice.Invoice;
import com.senang.bayar.core.setting.CreditCardPaymentFee;
import com.senang.bayar.core.setting.DirectDebitPaymentFee;
import org.javamoney.moneta.FastMoney;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Unit test - Invoice Service
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class InvoiceServiceTest {

    @org.springframework.context.annotation.Configuration
    static class Configuration {
        static RuleService ruleService = mock(RuleService.class);
        static CreditCardPaymentFee creditCardPaymentFee = mock(CreditCardPaymentFee.class);
        static DirectDebitPaymentFee directDebitPaymentFee = mock(DirectDebitPaymentFee.class);

        @Bean
        InvoiceService invoiceService() {
            return new InvoiceService();
        }

        @Bean
        RuleService ruleService() {
            return ruleService;
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

    @Test
    public void create() {
        Invoice invoice = invoiceService.create();
        assertThat(invoice.getFrom(), is("IPC"));
        assertThat(invoice.getTo(), is("1MDB"));
        assertThat(invoice.getMethod(), is(Invoice.Method.CREDIT));
        assertThat(invoice.getAmount(), is(FastMoney.of(1.0, "MYR")));
        assertThat(invoice.getCountry().getName(), is("Malaysia"));
    }
}