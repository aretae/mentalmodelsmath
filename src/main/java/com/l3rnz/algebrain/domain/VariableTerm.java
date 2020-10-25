package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;

/**
 * Variable Term holds variable data
 */

public class VariableTerm extends Term<String>{


    public VariableTerm(String data) {
        //TODO start using git
        handleBadData(data);
        separateDataAndNegatives(data);
    }

    private void handleBadData(String data) {
        if ( !data.matches("^[-]*[a-zA-Z]+$") ) {
            throw new ExpressionException();
        }
    }

    private void separateDataAndNegatives(String data) {
        int counter = 0;
        while (data.charAt(counter) == '-') {
            addNegative();
            counter++;
        }
        setData(data.substring(counter));
    }


    @Override
    public Term addValue(Term ex) {
        return null;
    }

    @Override
    public String getValue() {
        return null;
    }

}
