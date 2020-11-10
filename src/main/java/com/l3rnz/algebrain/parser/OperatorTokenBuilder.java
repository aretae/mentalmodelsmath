package com.l3rnz.algebrain.parser;

import com.l3rnz.algebrain.domain.ExpressionConstants;

public class OperatorTokenBuilder extends TokenBuilder {

    public OperatorTokenBuilder(char firstChar) {
        super(firstChar);
    }

    public boolean canAdd(char c) {
        if (checkForInequalities(c)) {
            return true;
        }
        return false;
    }

    boolean checkForInequalities(char c) {
        return c== ExpressionConstants.EQUALS_CHAR && builder.toString().length()==1
                && ExpressionConstants.INEQUALITY_PARTS_STRING.contains(builder.toString());
    }
}
