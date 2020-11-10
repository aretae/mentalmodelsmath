package com.l3rnz.algebrain.parser;

public class VariableTokenBuilder extends TokenBuilder {
    public VariableTokenBuilder(char firstChar) {
        super(firstChar);
    }

    public boolean canAdd(char c) {
        if ("abcdefghijklmnopqrstuvwxyz".indexOf(c)>=0) {
            return true;
        }
        return false;
    }
}
