package com.l3rnz.algebrain.parser;

import com.l3rnz.algebrain.domain.*;
import com.l3rnz.algebrain.exception.ExpressionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser2 {

    public static final Token MULTIPLY_TOKEN = new Token("*");
    public static final Token ADD_TOKEN = new Token("+");
    public static final Token SUBTRACT_TOKEN = new Token("-");
    public static final Token [] TOKENS_THAT_PRECURSE_NEGATIVE = new Token[] {
            MULTIPLY_TOKEN, ADD_TOKEN, SUBTRACT_TOKEN
    };

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
        if (isSum(tokens)) {
            returnExpression = parseSum(tokens);
        } else if (isProduct(tokens)) {
            returnExpression = parseProduct(tokens);
        } else {
            returnExpression = processTermAndNegative(tokens);
        }
        return returnExpression;
    }

    private Expression parseSum(List<Token> tokens) {
        Sum sum= new Sum();
        List<Token> currentTokenList = tokens;
        int indexOfSumSeparator;
        List<Token> subList = null;

        boolean explicit=true;
        int modifier;
        while (isSum(currentTokenList)) {
            indexOfSumSeparator = findFirstSplitIndex(currentTokenList);
            subList = currentTokenList.subList(0, indexOfSumSeparator);
            sum.addExpression(parse(subList),explicit);
            if (currentTokenList.get(indexOfSumSeparator).equals(SUBTRACT_TOKEN)) {
                modifier=0;
                explicit = false;
            } else {
                modifier = 1;
                explicit = true;
            }
            currentTokenList = currentTokenList.subList(indexOfSumSeparator+modifier, currentTokenList.size());
        }
        sum.addExpression(parse(currentTokenList), explicit);
        return sum;
    }

    private int findFirstSplitIndex(List<Token> currentTokenList) {
        int addIndex = currentTokenList.indexOf(ADD_TOKEN);
        int subtractIndex = findSubtractIndex(currentTokenList);
        int index = 0;
        if (subtractIndex < 0 || (addIndex >= 0 && addIndex < subtractIndex)) {
            index = addIndex;
        } else {
            index = subtractIndex;
        }
        return index;
    }

    private int findSubtractIndex(List<Token> currentTokenList) {
        Token token;
        for (int i = 1; i<currentTokenList.size(); i++) {
            token = currentTokenList.get(i);
            if (token.equals(SUBTRACT_TOKEN) && isNotJustNegative(currentTokenList,i)){
                return i;
            }
        }
        return -1;
    }

    private boolean isSum(List<Token> tokens) {
        return tokens.contains(ADD_TOKEN)
                || isDifference(tokens);
    }

    private boolean isDifference(List<Token> tokens) {
        if (! tokens.contains(SUBTRACT_TOKEN)) {
            return false;
        }
        for(int index = 1; index < tokens.size(); index++) {
            if (SUBTRACT_TOKEN.equals(tokens.get(index))
                    && isNotJustNegative(tokens, index)) {
                return true;
            }
        }
        return false;
    }

    private boolean isNotJustNegative(List<Token> tokens, int index) {
        return !Arrays.asList(TOKENS_THAT_PRECURSE_NEGATIVE).contains(tokens.get(index-1));
    }

    Expression parseProduct(List<Token> tokens) {
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
        return product;
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
