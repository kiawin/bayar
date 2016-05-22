package com.senang.bayar.core.provider;

import javax.money.MonetaryAmount;
import java.util.Map;

/**
 * Interface: Provider Fee
 */
public interface HasFee<F extends Float> {

    /**
     * Get fee (in percentage)
     *
     * @return
     */
    F getFee();

    /**
     * Set fee (in percentage)
     *
     * @param fee
     */
    void setFee(F fee);

    /**
     * Get rate (fee / 100)
     *
     * @return
     */
    F getRate();

    /**
     * Get map of fees (in percentage)
     * - key: payment method
     * - valid: fee in percentage
     *
     * @return
     */
    Map<String, Float> getFees();

    /**
     * Set map of fees
     * - key: payment method
     * - valid: fee in percentage
     *
     * @return
     */

    void setFees(Map<String, Float> fees);
}