package com.l3rnz.algebrain.parser;

import com.l3rnz.algebrain.domain.*;
import com.l3rnz.algebrain.exception.ExpressionException;

import java.util.ArrayList;
import java.util.List;

public class Parser2 {

    public static final Token MULTIPLY_TOKEN = new Token("*");

    enum TokenType {
        TERM(1),
        IMPLICIT_TERM(2),
        NEGATIVE(3),
        PARENS(4),
        FUNCTION(5),
        FRACTION(6),
        PRODUCT(7),
        SUM(8),
        EQUALITY(9);

        public final int precedence;
        private TokenType(int p) {
            precedence = p;
        }
    }

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
        Expression returnExpression;
        if (isProduct(tokens)) {
            returnExpression = parseProduct(tokens);
        } else {
            returnExpression = processTermAndNegative(tokens);
        }
        return returnExpression;
    }

    Expression parseProduct(List<Token> tokens) {
        Expression returnExpression;
        Product product = new Product();
        List<Token> currentTokenList = tokens;
        int indexOfMultiply;
        List<Token> subList = null;

        while (currentTokenList.contains(MULTIPLY_TOKEN)) {
            indexOfMultiply = currentTokenList.indexOf(MULTIPLY_TOKEN);
            subList = currentTokenList.subList(0, indexOfMultiply);
            product.addExpression(parse(subList));
            currentTokenList = currentTokenList.subList(indexOfMultiply+1, currentTokenList.size());
        }
        product.addExpression(parse(currentTokenList));
        returnExpression = product;
        return returnExpression;
    }

    private boolean isProduct(List<Token> tokens) {
        return tokens.contains(new Token("*"));
    }



    Expression processTermAndNegative(List<Token> tokens) {
        Expression returnExpression;
        String tokenString = tokens.get(0).toString();
        if (Term.MINUS_STRING.equals(tokenString)) {
            returnExpression = parse(tokens.subList(1, tokens.size()));
            ((Term) returnExpression).addNegative();
        } else if (tokenString.matches(IntegerTerm.INTEGER_TERM_REGEX)) {
            returnExpression = new IntegerTerm(tokenString);
        } else if (tokenString.matches(DecimalTerm.DECIMAL_TERM_REGEX)) {
            returnExpression = new DecimalTerm(tokenString);
        } else if (tokenString.matches(VariableTerm.VARIABLE_TERM_REGEX)) {
            returnExpression = new VariableTerm(tokenString);
        } else {
            throw new ExpressionException();
        }
        return returnExpression;
    }

    public int getIndexOfMostImportantToken() {
        return 0;
    }

}
