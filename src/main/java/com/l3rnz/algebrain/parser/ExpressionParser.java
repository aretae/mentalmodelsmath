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

    public static final char MINUS = '-';
    public Term parseTerm(final String input) {
        final int numberOfNegatives = calculateNumberOfNegatives(input);
        final String adjustedInput = input.substring(numberOfNegatives);
        final Term term = processTermWithoutNegatives(adjustedInput);
        addNegatives(term, numberOfNegatives);
        return term;
    }

    private void addNegatives(final Term term, final int numberOfNegatives) {
        for (int i = 0; i < numberOfNegatives; i++) {
            term.addNegative();
        }
    }

    private Term processTermWithoutNegatives(final String adjustedInput) {
        Term term = null;
        if (adjustedInput.matches("^[0-9]+$")) {
            term = new IntegerTerm(Integer.parseInt(adjustedInput));
        }
        if (adjustedInput.matches("^[0-9]+\\.[0-9]+$")) {
            term = new DecimalTerm(Double.parseDouble(adjustedInput));
        }
        if (!adjustedInput.matches("^[0-9]+\\.?[0-9]*$")) {
            throw new ExpressionException();
        }
        return term;
    }

    private int calculateNumberOfNegatives(final String input) {
        int numberOfNegatives = 0;
        for (int index = 0; index < input.length(); index++) {
            if (input.charAt(index) == MINUS) {
                numberOfNegatives++;
            }
        }
        return numberOfNegatives;
    }

    public Expression parse(final String input) {
        final List<Map.Entry<String, Boolean>> parts = splitSum(input);
        if (parts.size() == 1) {
            return parseTerm(input);
        } else {
            final Sum sum = new Sum();
            for (Map.Entry<String, Boolean> sub: parts) {
                sum.addTerm(parseTerm(sub.getKey()), sub.getValue());
            }
            return sum;
        }
    }

    private List<Map.Entry<String, Boolean>> splitSum(final String input) {
        final List<Map.Entry<String, Boolean>> sumParts = new ArrayList<>();
        final StringBuilder builder = new StringBuilder();

        boolean usesAdd = true;
        for (int index = 0; index < input.length(); index++) {
            usesAdd = processOneCharacter(input, sumParts, builder, usesAdd, index);
        }
        endPartAndAdd(sumParts, builder, usesAdd);
        return sumParts;
    }

    private boolean processOneCharacter(final String input, final List<Map.Entry<String, Boolean>> sumParts,
                                        final StringBuilder builder, final boolean usesAdd, final int index) {
        final char currentCharacter = input.charAt(index);
        final char priorCharacter = getPriorCharacter(input, index);
        boolean modifiableUsesAdd = processAddOperation(sumParts, builder, usesAdd, currentCharacter);
        modifiableUsesAdd =
                processSubtractOperation(sumParts, builder, modifiableUsesAdd, currentCharacter, priorCharacter);
        processOtherCharacters(builder, currentCharacter, priorCharacter);
        return modifiableUsesAdd;
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

    private boolean processAddOperation(final List<Map.Entry<String, Boolean>> sumParts, final StringBuilder builder,
                                        final boolean usesAdd, final char currentCharacter) {
        boolean modifiableUsesAdd = usesAdd;
        if (isAddOperation(currentCharacter)) {
            endPartAndAdd(sumParts, builder, usesAdd);
            modifiableUsesAdd = true;
        }
        return modifiableUsesAdd;
    }

    private boolean isSubtractOperation(final char currentCharacter, final char priorCharacter) {
        return currentCharacter == '-' && priorCharacter != '+' && priorCharacter != '-';
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

}
