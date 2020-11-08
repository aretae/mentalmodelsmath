package com.l3rnz.algebrain.parser;


import com.l3rnz.algebrain.domain.*;
import com.l3rnz.algebrain.exception.ExpressionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Transforms Strings into Expressions.
 */
public class ExpressionParser {


    public Expression parseAddend(final String input) {
        if (input.contains("*")) {
            return parseProduct(input);
        } else {
            return parseTerm(input);
        }
    }

    private Expression parseTerm(final String input) {
        Term term = null;
        if (input.matches(IntegerTerm.INTEGER_TERM_REGEX)) {
            term = new IntegerTerm(input);
        }
        if (input.matches(DecimalTerm.DECIMAL_TERM_REGEX)) {
            term = new DecimalTerm(input);
        }
        if (input.matches(VariableTerm.VARIABLE_TERM_REGEX)) {
            term = new VariableTerm(input);
        }
        if (input.matches(ImplicitProductTerm.IMPLICIT_PRODUCT_TERM_REGEX)) {
            term = new ImplicitProductTerm(input);
        }
        if (input.matches(ParentheticalTerm.PARENTHETICAL_TERM_REGEX)) {
            term = new ParentheticalTerm(input);
        }
        if (term == null) {
            throw new ExpressionException();
        }
        return term;
    }


    public Product parseProduct(final String input) {
        final Product product = new Product();
        final String[] parts = input.split("\\*");
        for (String part : parts) {
            final Expression e = parseTerm(part);
            product.addExpression(e);
        }
        return product;
    }


    public Expression parse(final String input) {
        ParseData parseData = new ParseData(input);
        final List<Map.Entry<String, Boolean>> parts = splitSum(parseData);
        if (parts.size() == 1) {
            return parseAddend(input);
        } else {
            final Sum sum = new Sum();
            for (Map.Entry<String, Boolean> sub : parts) {
                sum.addExpression(parseAddend(sub.getKey()), sub.getValue());
            }
            return sum;
        }
    }

    private List<Map.Entry<String, Boolean>> splitSum(final ParseData parseData) {
        final List<Map.Entry<String, Boolean>> sumParts = new ArrayList<>();

        boolean usesAdd = true;
        for (parseData.index = 0; parseData.index < parseData.dataToBeParsed.length(); parseData.index ++) {
            usesAdd = processOneCharacter(parseData, sumParts, usesAdd);
        }
        endPartAndAdd(sumParts, parseData.builder, usesAdd);
        return sumParts;
    }

    private boolean processOneCharacter(ParseData parseData, final List<Map.Entry<String, Boolean>> sumParts,
                                        final boolean usesAdd) {
        parseData.updateParenCount();

        boolean modifiableUsesAdd = processAddOperation(parseData, sumParts, usesAdd);
        modifiableUsesAdd =
                processSubtractOperation(sumParts, parseData.builder, modifiableUsesAdd, parseData.getCurrentCharacter(), parseData.getPriorCharacter());
        processOtherCharacters(parseData.builder, parseData.getCurrentCharacter(), parseData.getPriorCharacter());
        return modifiableUsesAdd;
    }

    private void checkForParenCount(ParseData parseData, char currentCharacter) {
        if (currentCharacter == '(') {
            parseData.parenthesesCount ++;
        } else if (currentCharacter == ')') {
            parseData.parenthesesCount --;
        }
    }

    private char getCurrentCharacter(String dataToBeParsed, int index) {
        return dataToBeParsed.charAt(index);
    }

    private char getPriorCharacter(final String input, final int index) {
        char priorCharacter = '+';
        if (index != 0) {
            priorCharacter = input.charAt(index - 1);
        }
        return priorCharacter;
    }

    private void processOtherCharacters(final StringBuilder builder, final char currentCharacter,
                                        final char priorCharacter) {
        if (!isAddOperation(currentCharacter) && !isSubtractOperation(currentCharacter, priorCharacter)) {
            builder.append(currentCharacter);
        }
    }

    private boolean processSubtractOperation(final List<Map.Entry<String, Boolean>> sumParts,
                                             final StringBuilder builder, final boolean usesAdd,
                                             final char currentCharacter, final char priorCharacter) {
        boolean modifiableUsesAdd = usesAdd;
        if (isSubtractOperation(currentCharacter, priorCharacter)) {
            endPartAndAdd(sumParts, builder, usesAdd);
            modifiableUsesAdd = false;
        }
        return modifiableUsesAdd;
    }

    private boolean processAddOperation(ParseData parseData, final List<Map.Entry<String, Boolean>> sumParts,
                                        final boolean usesAdd) {
        boolean modifiableUsesAdd = usesAdd;
        if (isAddOperation(parseData.getCurrentCharacter())) {
            endPartAndAdd(sumParts, parseData.builder, usesAdd);
            modifiableUsesAdd = true;
        }
        return modifiableUsesAdd;
    }

    private boolean isSubtractOperation(final char currentCharacter, final char priorCharacter) {
        return currentCharacter == '-' && isOperator(priorCharacter);
    }

    private boolean isOperator(final char priorCharacter) {
        return !("+-*/".contains(String.valueOf(priorCharacter)));
    }

    private boolean isAddOperation(final char currentCharacter) {
        return currentCharacter == '+';
    }

    private void endPartAndAdd(final List<Map.Entry<String, Boolean>> sumParts, final StringBuilder builder,
                               final boolean usesAdd) {
        final String entryString = (usesAdd ? "" : "-") + builder.toString();
        final Map.Entry<String, Boolean> pair = Map.entry(entryString, usesAdd);
        sumParts.add(pair);
        builder.delete(0, builder.length());
    }

    private static class ParseData {
        public ParseData(String input) {
            dataToBeParsed = input;
            builder = new StringBuilder();
        }
        public String dataToBeParsed;
        public StringBuilder builder;
        public int parenthesesCount;
        public int index;

        public char getCurrentCharacter() {
            return dataToBeParsed.charAt(index);
        }

        public char getPriorCharacter() {
            char priorCharacter = '+';
            if (index != 0) {
                priorCharacter = dataToBeParsed.charAt(index - 1);
            }
            return priorCharacter;
        }

        private void updateParenCount() {
            if (getCurrentCharacter()== '(') {
                parenthesesCount ++;
            } else if (getCurrentCharacter()== ')') {
                parenthesesCount --;
            }
        }

    }
}
