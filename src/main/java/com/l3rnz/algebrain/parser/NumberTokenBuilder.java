package com.l3rnz.algebrain.parser;

import com.l3rnz.algebrain.domain.ExpressionConstants;

public class NumberTokenBuilder extends TokenBuilder {

    public NumberTokenBuilder(char firstChar) {
        super(firstChar);
    }

    public boolean canAdd(char c) {
        if (ExpressionConstants.DIGITS_STRING.indexOf(c)>=0) {
            return true;
        }
        if (c== ExpressionConstants.DOT_CHAR && !builder.toString().contains(ExpressionConstants.DOT_STRING)) {
            return true;
        }
        return false;
    }
}
