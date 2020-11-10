package com.l3rnz.algebrain.parser;

import com.l3rnz.algebrain.domain.Token;

public class TokenBuilder {

    StringBuilder builder;

    public TokenBuilder(char c) {
        builder = new StringBuilder();
        builder.append(c);
    }

    public boolean tryToAddNextCharacter(char c) {
        if (canAdd(c)) {
            builder.append(c);
            return true;
        }
        return false;
    }

    public boolean canAdd(char c) {
        return false;
    }

    public Token getToken() {
        return new Token(builder.toString());
    }
}
