package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;

/**
 * Variable Term holds variable data.
 */

public class VariableTerm extends Term<String> {

    public VariableTerm(final String data) {
        handleBadData(data);
        separateDataAndNegatives(data);
    }

    private void handleBadData(final String data) {
        if (!data.matches("^[-]*[a-zA-Z]+$")) {
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
        handleBadAdd(term);
        Term returnValue = null;
        returnValue = handleCancelingNegatives(term, returnValue);
        //TODO ignore adding variables that work for a bit
        return returnValue;
    }

    private Term handleCancelingNegatives(Term term, Term returnValue) {
        if (term.getNegativeMultiplier()+this.getNegativeMultiplier() == 0) {
            returnValue = new IntegerTerm(0);
        }
        return returnValue;
    }

    private void handleBadAdd(Term term) {
        if (!(term instanceof VariableTerm)) {
            throw new ExpressionException();
        }
        VariableTerm variableTerm = (VariableTerm) term;
        if (!(data.equals(term.data))) {
            throw new ExpressionException();
        }
    }

    @Override
    public String getValue() {
        return null;
    }

}
