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

    //TODO ignore this for a bit
    @Override
    public Term addValue(final Term ex) {
        return null;
    }

    @Override
    public String getValue() {
        return null;
    }

}
