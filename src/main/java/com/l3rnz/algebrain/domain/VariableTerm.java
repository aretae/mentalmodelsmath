package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;

/**
 * Variable Term holds variable data.
 */

public class VariableTerm extends Term<String> {

    public VariableTerm(final String data) {
        super(data);
    }

    @Override
    public String convertToType(final String data) {
        return data;
    }

    @Override
    public void checkDataValidity(String data) {
        if (!data.matches(ExpressionConstants.VARIABLE_TERM_WITH_NEGATIVE_REGEX)) {
            throw new ExpressionException();
        }
    }

    private void handleBadData(final String data) {
        if (!data.matches(ExpressionConstants.VARIABLE_TERM_WITH_NEGATIVE_REGEX)) {
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
    public Expression addValue(final Expression term) {
        if (term instanceof ImplicitProductTerm) {
            return ((Term) term).addValue(this);
        }
        handleBadAdd(term);
        return handleAddingTerms(term);
    }

    private Term handleAddingTerms(Expression term) {
        Term returnValue = null;
        if (term instanceof VariableTerm) {
            switch (((Term) term).getNegativeMultiplier() + this.getNegativeMultiplier()) {
                case 0:
                    returnValue = new IntegerTerm(0);
                    break;
                case 2:
                    returnValue = new ImplicitProductTerm("2" + getData());
                    break;
                default:
                    returnValue = new ImplicitProductTerm("-2" + getData());
            }
        }
        return returnValue;
    }

    private void handleBadAdd(Expression term) {
        if (term instanceof VariableTerm) {
            if (!(getData().equals(((Term) term).getData()))) {
                throw new ExpressionException();
            }
        } else if (!(term instanceof ImplicitProductTerm)) {
            throw new ExpressionException();
        }
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
        final Product product = new Product();
        product.addExpression(this);
        product.addExpression(ex);
        return new ImplicitProductTerm(product);
    }

    Term handleMultiplyByIdentity(Term ex) {
        Term term = null;
        if (checkForMultiplyByZero(ex)) {
            term = new IntegerTerm(0);
        }
        if (checkForMultiplyByOne(ex)) {
            term = this;
        }
        if (checkForMultiplyByNegativeOne(ex)) {
            term = buildNegativeThis();
        }
        return term;
    }

    private Term buildNegativeThis() {
        final VariableTerm term2 = new VariableTerm(this.toString());
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
        return ("0".equals(ex.toString()))
            || (ex instanceof DecimalTerm && ((DecimalTerm) ex).getData() == 0);
    }

}
