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

    private Expression parseTerm(String input) {
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
        if (term == null) {
            throw new ExpressionException();
        }
        return term;
    }



    public Product parseProduct(final String input) {
        Product product = new Product();
        String [] parts = input.split("\\*");
        for (String part: parts) {
            Expression e = parseTerm(part);
            product.addExpression(e);
        }
        return product;
    }


    public Expression parse(final String input) {
        final List<Map.Entry<String, Boolean>> parts = splitSum(input);
        if (parts.size() == 1) {
            return parseAddend(input);
        } else {
            final Sum sum = new Sum();
            for (Map.Entry<String, Boolean> sub: parts) {
                sum.addExpression(parseAddend(sub.getKey()), sub.getValue());
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
        return currentCharacter == '-' && isOperator(priorCharacter);
    }

    private boolean isOperator(char priorCharacter) {
        return !("+-*/".contains(priorCharacter+""));
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
