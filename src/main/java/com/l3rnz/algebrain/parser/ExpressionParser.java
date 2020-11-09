package com.l3rnz.algebrain.parser;


import com.l3rnz.algebrain.domain.*;
import com.l3rnz.algebrain.exception.ExpressionException;

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
        SumParser sumParser = new SumParser(input);
        final List<Map.Entry<String, Boolean>> parts = sumParser.splitSum();
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


}
