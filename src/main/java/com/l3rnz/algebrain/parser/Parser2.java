package com.l3rnz.algebrain.parser;

import com.l3rnz.algebrain.domain.DecimalTerm;
import com.l3rnz.algebrain.domain.Expression;
import com.l3rnz.algebrain.domain.IntegerTerm;
import com.l3rnz.algebrain.domain.Token;

import java.util.ArrayList;
import java.util.List;

public class Parser2 {

    private int currentIndex;
    TokenBuilderFactory factory;

    public Parser2() {
        factory = new TokenBuilderFactory();
    }

    public List<Token> tokenize(String s) {
        ArrayList<Token> tokens = new ArrayList<Token>();
        currentIndex = 0;
        while (currentIndex < s.length()) {
            Token token = getNextToken(s);
            tokens.add(token);
            currentIndex++;
        }
        return tokens;
    }

    Token getNextToken(String s) {
        TokenBuilder builder = factory.createTokenBuilder(s.charAt(currentIndex));
        while (currentIndex+1 < s.length() && builder.tryToAddNextCharacter(s.charAt(currentIndex+1))) {
            currentIndex++;
        }
        return builder.getToken();
    }

    public Expression parse(String input) {
        List<Token> tokens = tokenize(input);
        String tokenString = tokens.get(0).toString();
        if (tokenString.matches(IntegerTerm.INTEGER_TERM_REGEX)) {
            return new IntegerTerm(tokenString);
        } else if (tokenString.matches(DecimalTerm.DECIMAL_TERM_REGEX)) {
            return new DecimalTerm(tokenString);
        } else {
            return null;
        }
    }
}
