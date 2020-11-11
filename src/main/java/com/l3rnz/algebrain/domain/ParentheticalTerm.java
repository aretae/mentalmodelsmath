package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;
import com.l3rnz.algebrain.parser.ExpressionParser;

public class ParentheticalTerm extends Term<Expression>{

    public ParentheticalTerm(Expression expression) {
        setData(expression);
    }

    public ParentheticalTerm(String input) {
        if (!input.matches(ExpressionConstants.PARENTHETICAL_TERM_REGEX) && checkThatParenthesesMatch(input)) {
            throw new ExpressionException();
        }
        setData(new ExpressionParser().parse(input.substring(1, input.length()-1)));
    }

    static boolean checkThatParenthesesMatch(String input) {
        boolean parenthesesMatch = true;
        int openParenCount = 0;
        int closeParenCount = 0;
        for (char character: input.toCharArray()) {
            if (character == '(') {
                openParenCount++;
            }
            if (character == ')') {
                closeParenCount++;
            }
            if (openParenCount - closeParenCount < 0) {
                parenthesesMatch = false;
            }
        }
        if (openParenCount != closeParenCount) {
            parenthesesMatch = false;
        }
        return parenthesesMatch;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append(getData().toString());
        builder.append(")");
        return builder.toString();
    }

    @Override
    public Expression addValue(Expression ex) {
        Sum sum = new Sum();
        sum.addExpression(getData());
        sum.addExpression(ex);
        return sum;
    }

    @Override
    public Term multiplyValue(Term ex) {
        return null;
    }

    @Override
    public Expression convertToType(String data) {
        return null;
    }

    @Override
    public void checkDataValidity(String data) {

    }
}
