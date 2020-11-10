package com.l3rnz.algebrain.parser;

import com.l3rnz.algebrain.exception.ExpressionException;

public class TokenBuilderFactory {

    public static final String DECIMAL_REGEX = "[.]";
    public static final String NUMBER_REGEX = "[0-9]";
    public static final String VARIABLE_START_REGEX = "[A-Z]";
    public static final String VARABLE_CONTINUE_REGEX = "[a-z]";
    public static final String OPERATOR_REGEX = "[+-*/^()]"; // How to handle brackets

    public TokenBuilder createTokenBuilder(char firstChar) {
        TokenBuilder builder;
        if("0123456789".indexOf(firstChar)>=0) {
            builder = new NumberTokenBuilder(firstChar);
        } else if ( "ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(firstChar)>=0) {
            builder = new VariableTokenBuilder(firstChar);
        } else if ("+-*/^()[]=><!".indexOf(firstChar)>=0) {
            builder = new OperatorTokenBuilder(firstChar);
        } else {
            throw new ExpressionException();
        }
        return builder;
    }
}
