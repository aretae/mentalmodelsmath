package com.l3rnz.algebrain.parser;

public class NumberTokenBuilder extends TokenBuilder {
    public NumberTokenBuilder(char firstChar) {
        super(firstChar);
    }

    public boolean canAdd(char c) {
        if ("01234567890".indexOf(c)>=0) {
            return true;
        }
        if (c=='.' && !builder.toString().contains(".")) {
            return true;
        }
        return false;
    }
}
