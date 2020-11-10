package com.l3rnz.algebrain.parser;

import com.l3rnz.algebrain.domain.ExpressionConstants;

public class VariableTokenBuilder extends TokenBuilder {

    public VariableTokenBuilder(char firstChar) {
        super(firstChar);
    }

    public boolean canAdd(char c) {
        if (ExpressionConstants.ALPHABET_LOWER_CASE_STRING.indexOf(c)>=0) {
            return true;
        }
        return false;
    }
}
