package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;

/**
 * Variable Term holds variable data.
 */

public class VariableTerm extends Term<String> {

    public static final String VARIABLE_TERM_REGEX = "^[-]*[A-Z][a-z]*$";

    public VariableTerm(final String data) {
        super(data);
//        handleBadData(data);
//        separateDataAndNegatives(data);
    }

    @Override
    public String convertToType(final String data) {
        return data;
    }

    @Override
    public void checkDataValidity(String data) {
        if (!data.matches(VARIABLE_TERM_REGEX)) {
            throw new ExpressionException();
        }
    }

    private void handleBadData(final String data) {
        if (!data.matches(VARIABLE_TERM_REGEX)) {
            throw new ExpressionException();
        }
    }

    private void separateDataAndNegatives(final String data) {
        int counter = 0;
        while (data.charAt(counter) == '-') {
            addNegative();
            counter++;
        }
        setData(data.substring(counter));
    }

    @Override
    public Term addValue(final Term term) {
        if (term instanceof ImplicitProductTerm) {
            return term.addValue(this);
        }
        handleBadAdd(term);
        return handleAddingTerms(term);
    }

    private Term handleAddingTerms(Term term) {
        Term returnValue = null;
        if (term instanceof VariableTerm) {
            switch (term.getNegativeMultiplier() + this.getNegativeMultiplier()) {
                case 0:
                    returnValue = new IntegerTerm(0);
                    break;
                case 2:
                    returnValue = new ImplicitProductTerm("2" + getData());
                    break;
                case -2:
                    returnValue = new ImplicitProductTerm("-2" + getData());
            }
        }
        return returnValue;
    }

    private void handleBadAdd(Term term) {
        if (term instanceof VariableTerm) {
            if (!(getData().equals(term.getData()))) {
                throw new ExpressionException();
            }
        } else if (! (term instanceof ImplicitProductTerm)) {
            throw new ExpressionException();
        }
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public Term multiplyValue(Term ex) {
        Term term2 = handleMultiplyByIdentity(ex);
        if (term2 == null) {
            term2 = handleNormalMultiply(ex);
        }
        return term2;
    }



    Term handleNormalMultiply(Term ex) {
        Product product = new Product();
        product.addExpression(this);
        product.addExpression(ex);
        return new ImplicitProductTerm(product);
    }

    Term handleMultiplyByIdentity(Term ex) {
        if (checkForMultiplyByZero(ex)) {
            return new IntegerTerm(0);
        }
        if (checkForMultiplyByOne(ex)) {
            return this;
        }
        if (checkForMultiplyByNegativeOne(ex)) {
            return buildNegativeThis();
        }
        return null;
    }

    private Term buildNegativeThis() {
        VariableTerm term2 = new VariableTerm(this.toString());
        term2.resetNegatives();
        if (getNegativeMultiplier() == 1) {
            term2.addNegative();
        }
        return term2;
    }

    boolean checkForMultiplyByNegativeOne(Term ex) {
        return "-1".equals(ex.toString());
    }

    boolean checkForMultiplyByOne(Term ex) {
        return "1".equals(ex.toString());
    }


    boolean checkForMultiplyByZero(Term ex) {
        if ("0".equals(ex.toString())) {
            return true;
        } else if (ex instanceof DecimalTerm && ((DecimalTerm) ex).getValue() == 0) {
            return true;
        }
        return false;
    }

}
