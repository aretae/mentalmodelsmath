package com.l3rnz.algebrain.domain;

import java.util.Objects;

public class Token {
    public static final Token MULTIPLY_TOKEN = new Token("*");
    public static final Token ADD_TOKEN = new Token("+");
    public static final Token SUBTRACT_TOKEN = new Token("-");
    public static final Token [] TOKENS_THAT_PRECURSE_NEGATIVE = new Token[] {
            MULTIPLY_TOKEN, ADD_TOKEN, SUBTRACT_TOKEN
    };

    String content;

    public Token(String s) {
        content = s;
    }

    public String toString() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(content, token.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    public boolean isDecimal() {
        return isType(ExpressionConstants.DECIMAL_TERM_REGEX);
    }

    public boolean isVariable() {
        return isType(ExpressionConstants.VARIABLE_TERM_WITH_NEGATIVE_REGEX);
    }

    public boolean isInteger() {
        return isType(ExpressionConstants.INTEGER_TERM_REGEX);
    }

    boolean isType(String termRegex) {
        return content != null && content.matches(termRegex);
    }
}
