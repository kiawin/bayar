package com.senang.bayar.core.setting;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Provider payment fee - Direct Debit
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="paymentProviderFees.direct")
public class DirectDebitPaymentFee extends PaymentFee {
}