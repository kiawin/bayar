package com.senang.bayar.provider;

import com.neovisionaries.i18n.CountryCode;
import com.senang.bayar.RoutingApplication;
import com.senang.bayar.core.provider.*;
import com.senang.bayar.core.setting.CreditCardPaymentFee;
import com.senang.bayar.core.invoice.Invoice.Type;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit test - Credit Card Payment Providers
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RoutingApplication.class)
public class CreditCardPaymentProviderTest {

    CreditCardPaymentFee creditCardPaymentFee;

    @Before
    public void setUp() {
        Map<String, Float> paypal = new HashMap<String, Float>() {{
            put("default", 22.1f);
            put("visa", 22.2f);
        }};
        Map<String, Float> maybank = new HashMap<String, Float>() {{
            put("default", 33.1f);
            put("amex", 33.2f);
            put("visa", 33.3f);
        }};
        Map<String, Float> hsbc = new HashMap<String, Float>() {{
            put("default", 44.1f);
        }};
        Map<String, Float> generic = new HashMap<String, Float>() {{
            put("default", 55.1f);
        }};
        creditCardPaymentFee = new CreditCardPaymentFee(paypal, maybank, hsbc, generic);
    }

    @Test
    public void hsbc() {
        CountryCode country = CountryCode.valueOf("MY");
        Map<String, Float> fees = creditCardPaymentFee.getHsbc();

        Type type;
        CreditCardPaymentProvider paymentProvider;

        // 1. Fallback to default
        type = Type.MASTER;
        paymentProvider = new HsbcCreditCardPaymentProvider(type, country, fees);
        assertThat(paymentProvider.getFee(), is(44.1f));
    }

    @Test
    public void maybank() {
        CountryCode country = CountryCode.valueOf("MY");
        Map<String, Float> fees = creditCardPaymentFee.getMaybank();

        Type type;
        CreditCardPaymentProvider paymentProvider;

        // 1. Amex
        type = Type.AMEX;
        paymentProvider = new MaybankCreditCardPaymentProvider(type, country, fees);
        assertThat(paymentProvider.getFee(), is(33.2f));

        // 2. Fallback to default
        type = Type.MASTER;
        paymentProvider = new MaybankCreditCardPaymentProvider(type, country, fees);
        assertThat(paymentProvider.getFee(), is(33.1f));
    }

    @Test
    public void paypal() {
        CountryCode country = CountryCode.valueOf("MY");
        Map<String, Float> fees = creditCardPaymentFee.getPaypal();

        Type type;
        CreditCardPaymentProvider paymentProvider;

        // 1. Visa
        type = Type.VISA;
        paymentProvider = new PaypalCreditCardPaymentProvider(type, country, fees);
        assertThat(paymentProvider.getFee(), is(22.2f));

        // 2. Fallback to default
        type = Type.MASTER;
        paymentProvider = new PaypalCreditCardPaymentProvider(type, country, fees);
        assertThat(paymentProvider.getFee(), is(22.1f));
    }

    @Test
    public void generic() {
        CountryCode country = CountryCode.valueOf("MY");
        Map<String, Float> fees = creditCardPaymentFee.getGeneric();

        Type type;
        CreditCardPaymentProvider paymentProvider;

        // 1. Fallback to default
        type = Type.MASTER;
        paymentProvider = new GenericCreditCardPaymentProvider(type, country, fees);
        assertThat(paymentProvider.getFee(), is(55.1f));
    }
}