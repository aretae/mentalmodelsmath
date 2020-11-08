package com.l3rnz.algebrain.domain;

import java.math.BigDecimal;

/**
 * Superclass for IntegerTerm and DecimalTerm.
 * @param <T> Type of numeric Term:  Integer or Double
 */
public abstract class NumericTerm<T> extends Term<T> {

    public NumericTerm(final T data) {
        if (checkNegative(data)) {
            setData(data);
            negativeCount = 0;
        } else {
            setData(makeNegative(data));
            negativeCount = 1;
        }
    }

    public NumericTerm(final String data) {
        super(data);
    }

    public abstract boolean checkNegative(T t);
    public abstract T makeNegative(T t);

    public BigDecimal getBigDecimal() {
        final BigDecimal value = new BigDecimal(new String(String.valueOf(getData())));
        final BigDecimal negative = new BigDecimal(getNegativeMultiplier());
        return value.multiply(negative);
    }
}
