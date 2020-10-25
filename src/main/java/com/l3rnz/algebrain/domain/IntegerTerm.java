package com.l3rnz.algebrain.domain;


import com.l3rnz.algebrain.exception.ExpressionException;

/**
 * Class to represent a single integer Term including negatives.
 * example: 3, -4, --5, etc.
 */
public class IntegerTerm extends Term<Integer> {

    public IntegerTerm(final Integer data) {
        if ((double) data >= 0) {
            this.data = data;
            negativeCount = 0;
        } else {
            this.data = -1 * data;
            negativeCount = 1;
        }
    }

    @Override
    public Integer getValue() {
        return getNegativeMultiplier() * data.intValue();
    }

    @Override
    public Term addValue(final Term ex) {
        if (ex instanceof IntegerTerm) {
            return new IntegerTerm(getValue() + ((IntegerTerm) ex).getValue());
        } else if (ex instanceof DecimalTerm) {
            return new DecimalTerm(getValue() + ((DecimalTerm) ex).getValue());
        } else {
            throw new ExpressionException();
        }
    }




}
