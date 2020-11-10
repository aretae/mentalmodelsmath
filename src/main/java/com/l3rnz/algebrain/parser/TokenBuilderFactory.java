package com.l3rnz.algebrain.parser;

import com.l3rnz.algebrain.domain.ExpressionConstants;
import com.l3rnz.algebrain.exception.ExpressionException;

public class TokenBuilderFactory {

    public TokenBuilder createTokenBuilder(char firstChar) {
        TokenBuilder builder;
        if(ExpressionConstants.DIGITS_STRING.indexOf(firstChar)>=0) {
            builder = new NumberTokenBuilder(firstChar);
        } else if ( ExpressionConstants.ALPHABET_CAPITALS_STRING.indexOf(firstChar)>=0) {
            builder = new VariableTokenBuilder(firstChar);
        } else if (ExpressionConstants.OPERATORS_STRING.indexOf(firstChar)>=0) {
            builder = new OperatorTokenBuilder(firstChar);
        } else {
            throw new ExpressionException();
        }
        return builder;
    }
}
