package com.l3rnz.algebrain.parser;

import com.l3rnz.algebrain.domain.*;
import com.l3rnz.algebrain.exception.ExpressionException;

import java.util.ArrayList;
import java.util.Arrays;
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
        while (currentIndex + 1 < s.length() && builder.tryToAddNextCharacter(s.charAt(currentIndex + 1))) {
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
        } else if (isImplcitProduct(tokens)) {
            returnExpression = parseImplicitProduct(tokens);
        } else {
            returnExpression = processTermAndNegative(tokens);
        }
        return returnExpression;
    }

    private boolean isImplcitProduct(List<Token> tokens) {
        if (tokens.size() < 2) {
            return false;
        }
        for (int index = 1; index < tokens.size(); index++) {
            Token token1 = tokens.get(index - 1);
            Token token2 = tokens.get(index);
            if (shouldntFollow(token1, token2)) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldntFollow(Token token1, Token token2) {
        if (token1.isTerm() && token2.isVariable()) {
            return true;
        }
        return false;
    }

    private Expression parseImplicitProduct(List<Token> tokens) {
        Product product = new Product();
        int start = 0;
        for (int index = 1; index < tokens.size(); index++ ) {
            Token token1 = tokens.get(index - 1);
            Token token2 = tokens.get(index);
            if (shouldntFollow(token1, token2)) {
                product.addExpression(parse(tokens.subList(start, index)));
                start = index;
            }
        }
        product.addExpression(parse(tokens.subList(start, tokens.size())));
        ImplicitProductTerm term = new ImplicitProductTerm(product);
        return term;
    }


    private Expression parseSum(List<Token> tokens) {
        Sum sum = new Sum();
        List<Token> currentTokenList = tokens;
        int indexOfSumSeparator;
        List<Token> subList = null;

        boolean explicit = true;
        int modifier;
        while (isSum(currentTokenList)) {
            indexOfSumSeparator = findFirstSplitIndex(currentTokenList);
            subList = currentTokenList.subList(0, indexOfSumSeparator);
            sum.addExpression(parse(subList), explicit);
            if (currentTokenList.get(indexOfSumSeparator).equals(Token.SUBTRACT_TOKEN)) {
                modifier = 0;
                explicit = false;
            } else {
                modifier = 1;
                explicit = true;
            }
            currentTokenList = currentTokenList.subList(indexOfSumSeparator + modifier, currentTokenList.size());
        }
        sum.addExpression(parse(currentTokenList), explicit);
        return sum;
    }

    private int findFirstSplitIndex(List<Token> currentTokenList) {
        int addIndex = currentTokenList.indexOf(Token.ADD_TOKEN);
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
        for (int i = 1; i < currentTokenList.size(); i++) {
            token = currentTokenList.get(i);
            if (token.equals(Token.SUBTRACT_TOKEN) && isNotJustNegative(currentTokenList, i)) {
                return i;
            }
        }
        return -1;
    }

    private boolean isSum(List<Token> tokens) {
        return tokens.contains(Token.ADD_TOKEN)
                || isDifference(tokens);
    }

    private boolean isDifference(List<Token> tokens) {
        if (!tokens.contains(Token.SUBTRACT_TOKEN)) {
            return false;
        }
        for (int index = 1; index < tokens.size(); index++) {
            if (Token.SUBTRACT_TOKEN.equals(tokens.get(index))
                    && isNotJustNegative(tokens, index)) {
                return true;
            }
        }
        return false;
    }

    private boolean isNotJustNegative(List<Token> tokens, int index) {
        return !Arrays.asList(Token.TOKENS_THAT_PRECURSE_NEGATIVE).contains(tokens.get(index - 1));
    }

    Expression parseProduct(List<Token> tokens) {
        Product product = new Product();
        List<Token> currentTokenList = tokens;
        int indexOfMultiply;
        List<Token> subList = null;

        while (currentTokenList.contains(Token.MULTIPLY_TOKEN)) {
            indexOfMultiply = currentTokenList.indexOf(Token.MULTIPLY_TOKEN);
            subList = currentTokenList.subList(0, indexOfMultiply);
            product.addExpression(parse(subList));
            currentTokenList = currentTokenList.subList(indexOfMultiply + 1, currentTokenList.size());
        }
        product.addExpression(parse(currentTokenList));
        return product;
    }

    private boolean isProduct(List<Token> tokens) {
        return tokens.contains(new Token("*"));
    }


    Expression processTermAndNegative(List<Token> tokens) {
        Expression returnExpression;
        Token token = tokens.get(0);
        if (ExpressionConstants.MINUS_STRING.equals(token.toString())) {
            returnExpression = parse(tokens.subList(1, tokens.size()));
            ((Term) returnExpression).addNegative();
        } else if (token.isInteger()) {
            returnExpression = new IntegerTerm(token.toString());
        } else if (token.isDecimal()) {
            returnExpression = new DecimalTerm(token.toString());
        } else if (token.isVariable()) {
            returnExpression = new VariableTerm(token.toString());
        } else {
            throw new ExpressionException();
        }
        return returnExpression;
    }


}
