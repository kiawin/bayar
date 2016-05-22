package com.senang.bayar.provider;

import com.senang.bayar.RoutingApplication;
import com.senang.bayar.core.provider.DirectDebitPaymentProvider;
import com.senang.bayar.core.provider.GenericDirectDebitPaymentProvider;
import com.senang.bayar.core.invoice.Invoice.Type;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit test - Direct Debit Payment Providers
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RoutingApplication.class)
public class DirectDebitPaymentProviderTest {

    @Test
    public void generic() {
        Type type = Type.DEFAULT;
        DirectDebitPaymentProvider paymentProvider = new GenericDirectDebitPaymentProvider(type);
        assertThat(paymentProvider.getType(), is(type));
    }
}
