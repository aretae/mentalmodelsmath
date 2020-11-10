package com.l3rnz.algebrain.parser;

import com.l3rnz.algebrain.domain.*;

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
        return parse(tokens);
    }

    public Expression parse(List<Token> tokens) {
        String tokenString = null;
        Expression returnExpression = null;
        int index = getIndexOfMostImportantToken();
        tokenString = tokens.get(index).toString();
        if (Term.MINUS_STRING.equals(tokenString)) {
            returnExpression = parse(tokens.subList(index+1, tokens.size()));
            ((Term) returnExpression).addNegative();
        }
        if (tokenString.matches(IntegerTerm.INTEGER_TERM_REGEX)) {
            returnExpression = new IntegerTerm(tokenString);
        } else if (tokenString.matches(DecimalTerm.DECIMAL_TERM_REGEX)) {
            returnExpression = new DecimalTerm(tokenString);
        } else if (tokenString.matches(VariableTerm.VARIABLE_TERM_REGEX)) {
            returnExpression = new VariableTerm(tokenString);
        }
        return returnExpression;
    }

    public int getIndexOfMostImportantToken() {
        return 0;
    }

}
