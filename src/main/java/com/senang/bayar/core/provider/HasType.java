package com.senang.bayar.core.provider;

/**
 * Interface: Provider Payment Type
 */
public interface HasType<T extends Enum> {

    /**
     * Get type enum
     *
     * @return
     */
    T getType();

    /**
     * Set type enum
     *
     * @param type
     */
    void setType(T type);

    /**
     * Get type (in string)
     *
     * @return
     */
    String getTypeName();

    /**
     * Get default type (in string)
     *
     * @return
     */
    String getDefaultTypeName();
}