package com.l3rnz.algebrain.parser;

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
        return c=='=' && builder.toString().length()==1 && "><!".contains(builder.toString());
    }
}
