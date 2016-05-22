package com.senang.bayar.service;

import com.neovisionaries.i18n.CountryCode;
import com.senang.bayar.core.invoice.Invoice;
import com.senang.bayar.core.invoice.Invoice.Type;
import com.senang.bayar.core.invoice.Invoice.Method;
import org.javamoney.moneta.FastMoney;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Invoice Service
 */
@Component
public class InvoiceService {

    @Value("${from}")
    private String from;

    @Value("${to}")
    private String to;

    @Value("${method}")
    private String method;

    @Value("${type:default}")
    private String type;

    @Value("${amount}")
    private float amount;

    @Value("${country}")
    private String country;

    @Value("${bank:}")
    private String bank;

    private static final List<Type> CREDIT_CARD_SUPPORTED_TYPE = new ArrayList<Type>() {{
        add(Type.DEFAULT);
        add(Type.VISA);
        add(Type.MASTER);
        add(Type.AMEX);
    }};

    private static final List<Type> DIRECT_DEBIT_SUPPORTED_TYPE = new ArrayList<Type>() {{
        add(Type.DEFAULT);
    }};

    public Invoice create() {
        Method method = Method.valueOf(this.method.toUpperCase());
        Type type = Invoice.Type.valueOf(this.type.toUpperCase());
        validateType(type);

        CountryCode country = CountryCode.valueOf(this.country.toUpperCase());
        FastMoney amount = FastMoney.of(this.amount, country.getCurrency().getCurrencyCode());

        Invoice invoice = new Invoice(from, to, method, type, amount, country, bank);

        return invoice;
    }

    private void validateType(Type type) {
        if (method.equals(Method.CREDIT) && !CREDIT_CARD_SUPPORTED_TYPE.contains(type)
                || method.equals(Method.DIRECT) && !DIRECT_DEBIT_SUPPORTED_TYPE.contains(type)) {
            throw new IllegalArgumentException("Unsupported Invoice Payment Type");
        }
    }
}
