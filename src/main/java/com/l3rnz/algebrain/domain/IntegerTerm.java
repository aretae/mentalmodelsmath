package com.l3rnz.algebrain.domain;


import com.l3rnz.algebrain.exception.ExpressionException;

/**
 * Class to represent a single integer Term including negatives.
 * example: 3, -4, --5, etc.
 */
public class IntegerTerm extends NumericTerm<Integer> {

    public static final String INTEGER_TERM_REGEX = "^[-]*[0-9]+$";

    public IntegerTerm(final Integer data) {
        super(data);
    }

    public IntegerTerm(final String data) {
        super(data);
    }

    @Override
    public Integer convertToType(String data){
        return Integer.parseInt(data);
    }

    @Override
    public void checkDataValidity(String data) {
        if (!data.matches(INTEGER_TERM_REGEX)) {
            throw new ExpressionException();
        }
    }

    @Override
    public boolean checkNegative(Integer data) {
        return (int) data >= 0;
    }

    @Override
    public Integer makeNegative(Integer data) {
        return -1 * data;
    }

    @Override
    public Integer getValue() {
        return getNegativeMultiplier() * getData().intValue();
    }

    @Override
    public Term addValue(final Term ex) {
        if (ex instanceof IntegerTerm) {
            return new IntegerTerm(getValue() + ((IntegerTerm) ex).getValue());
        } else if (ex instanceof DecimalTerm) {
            DecimalTerm dt = (DecimalTerm) ex;
            return new DecimalTerm(dt.getSumWith(this));
        } else {
            throw new ExpressionException();
        }
    }

    @Override
    public Term multiplyValue(final Term ex) {
        if (ex instanceof IntegerTerm) {
            return new IntegerTerm(getValue() * ((IntegerTerm) ex).getValue());
        } else if (ex instanceof DecimalTerm) {
            DecimalTerm dt = (DecimalTerm) ex;
            return new DecimalTerm(dt.getProductWith(this));
        } else if (ex instanceof VariableTerm){
            return ex.multiplyValue(this);
        } else {
            throw new ExpressionException();
        }
    }


}
