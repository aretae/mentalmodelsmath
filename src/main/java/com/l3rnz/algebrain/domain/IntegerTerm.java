package com.l3rnz.algebrain.domain;


import com.l3rnz.algebrain.exception.ExpressionException;

/**
 * Class to represent a single integer Term including negatives.
 * example: 3, -4, --5, etc.
 */
public class IntegerTerm extends NumericTerm<Integer> {

    public IntegerTerm(final Integer data) {
        super(data);
    }

    public IntegerTerm(final String data) {
        super(data);
    }

    @Override
    public Integer convertToType(final String data) {
        return Integer.parseInt(data);
    }

    @Override
    public void checkDataValidity(final String data) {
        if (!data.matches(ExpressionConstants.INTEGER_TERM_REGEX)) {
            throw new ExpressionException();
        }
    }

    @Override
    public boolean checkNegative(final Integer data) {
        return (int) data >= 0;
    }

    @Override
    public Integer makeNegative(final Integer data) {
        return -1 * data;
    }

    @Override
    public Term addValue(final Term ex) {
        Term term = null;
        if (ex instanceof IntegerTerm) {
            term = new IntegerTerm(getData() + ((IntegerTerm) ex).getData());
        }
        if (ex instanceof DecimalTerm) {
            final DecimalTerm dt = (DecimalTerm) ex;
            term = new DecimalTerm(dt.getSumWith(this));
        }
        if (term == null) {
            throw new ExpressionException();
        }
        return term;
    }

    @Override
    public Term multiplyValue(final Term ex) {
        Term term = null;
        if (ex instanceof IntegerTerm) {
            term = new IntegerTerm(getData() * ((IntegerTerm) ex).getData());
        }
        if (ex instanceof DecimalTerm) {
            final DecimalTerm dt = (DecimalTerm) ex;
            term = new DecimalTerm(dt.getProductWith(this));
        }
        if (ex instanceof VariableTerm) {
            term = ex.multiplyValue(this);
        }
        if (term == null) {
            throw new ExpressionException();
        }
        return term;
    }


}
