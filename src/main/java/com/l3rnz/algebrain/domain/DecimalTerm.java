package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;

import java.math.BigDecimal;

/**
 * Terms that handle Decimals.
 */
public class DecimalTerm extends Term<Double> {
    public DecimalTerm(final Double data) {
        if (data >= 0) {
            this.data = data;
            negativeCount = 0;
        } else {
            this.data = -1 * data;
            negativeCount = 1;
        }
    }

    @Override
    public Double getValue() {
        return getNegativeMultiplier() * data.doubleValue();
    }

    @Override
    public Term addValue(final Term ex) {
        if (ex instanceof IntegerTerm) {
            return new DecimalTerm(getValue() + ((IntegerTerm) ex).getValue());
        } else if (ex instanceof DecimalTerm) {
            return new DecimalTerm(getSumWith((DecimalTerm) ex));
        } else {
            throw new ExpressionException();
        }
    }

    private double getSumWith(final DecimalTerm ex) {
        final BigDecimal dec1 = new BigDecimal(this.toString());
        final BigDecimal dec2 = new BigDecimal(ex.toString());
        return dec1.add(dec2).doubleValue();
    }
}
