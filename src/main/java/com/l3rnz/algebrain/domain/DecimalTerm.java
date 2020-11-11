package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;

import java.math.BigDecimal;


/**
 * Terms that handle Decimals.
 */
public class DecimalTerm extends NumericTerm<Double> {

    public DecimalTerm(final Double data) {
        super(data);
    }

    public DecimalTerm(final String data) {
        super(data);

    }

    @Override
    public Double convertToType(final String data) {
        return Double.parseDouble(data);
    }

    @Override
    public void checkDataValidity(final String data) {
        if (!data.matches(ExpressionConstants.DECIMAL_TERM_REGEX)) {
            throw new ExpressionException();
        }
    }

    @Override
    public boolean checkNegative(final Double data) {
        return (double) data >= 0;
    }

    @Override
    public Double makeNegative(final Double data) {
        return -1 * data;
    }

    @Override
    public Expression addValue(final Expression ex) {
        if (ex instanceof IntegerTerm) {
            return new DecimalTerm(getSumWith((IntegerTerm) ex));
        } else if (ex instanceof DecimalTerm) {
            return new DecimalTerm(getSumWith((DecimalTerm) ex));
        } else {
            throw new ExpressionException();
        }
    }

    @Override
    public Term multiplyValue(final Term ex) {
        Term term = null;
        if (ex instanceof IntegerTerm) {
            term = new DecimalTerm(getProductWith((IntegerTerm) ex));
        }
        if (ex instanceof DecimalTerm) {
            term = new DecimalTerm(getProductWith((DecimalTerm) ex));
        }
        if (ex instanceof VariableTerm) {
            term = ex.multiplyValue(this);
        }
        if (term == null) {
            throw new ExpressionException();
        }
        return term;
    }

    public double getProductWith(final NumericTerm ex) {
        final BigDecimal dec1 = getBigDecimal();
        final BigDecimal dec2 = ex.getBigDecimal();
        return dec1.multiply(dec2).doubleValue();
    }

    public double getSumWith(final NumericTerm ex) {
        final BigDecimal dec1 = getBigDecimal();
        final BigDecimal dec2 = ex.getBigDecimal();
        return dec1.add(dec2).doubleValue();
    }
}
