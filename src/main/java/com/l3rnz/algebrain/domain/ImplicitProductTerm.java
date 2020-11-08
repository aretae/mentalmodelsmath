package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Term to represent an product without multiplication symbols.
 */
public class ImplicitProductTerm extends Term<List<Expression>> {

    public static final String IMPLICIT_PRODUCT_TERM_REGEX = "^[-]*(([0-9]+([.][0-9]+)?)|([A-Z][a-z]*))([A-Z][a-z]*)+$";
    public static final String MINUS_SIGN = "-";
    public static final String EMPTY_STRING = "";

    public ImplicitProductTerm(final String input) {
        super(input);
    }

    public ImplicitProductTerm(Product inputProduct) {
        final List<Expression> variables = new ArrayList<>();
        ProductPart pPart;
        Expression expression;
        for (IndefiniteSizeExpressionPart term : inputProduct.terms) {
            pPart = (ProductPart) term;
            expression = pPart.getContainedExpression();
            if (expression instanceof NumericTerm) {
                variables.add(0, expression);
            } else {
                variables.add(expression);
            }
        }
        if (!(variables.get(0) instanceof NumericTerm)) {
            variables.add(0, new IntegerTerm(1));
        }
        setData(variables);
        processNegatives();
    }

    private void processNegatives() {
        if (checkForAndStripNegatives()) {
            addNegative();
        }
    }

    private boolean checkForAndStripNegatives() {
        boolean swapNegative = false;
        Term term = null;
        for (Expression part : getData()) {
            if (!(part instanceof Term)) {
                continue;
            }
            term = (Term) part;
            if (term.getNegativeCount() % 2 == 1) {
                swapNegative = !swapNegative;
            }
            term.resetNegatives();
        }
        return swapNegative;
    }

    @Override
    public void checkDataValidity(String input) {
        if (!input.matches(IMPLICIT_PRODUCT_TERM_REGEX)) {
            throw new ExpressionException();
        }
    }

    @Override
    public List<Expression> convertToType(final String input) {
        final List<Expression> data = new ArrayList<>();
        processNumberTermFromString(input, data);
        processVariablesFromString(input, data);
        return data;
    }

    @Override
    public Term addValue(Term ex) {
        Term returnTerm = null;
        if (checkForAddableVariableTerm(ex)) {
            returnTerm = addWithoutChecks((VariableTerm) ex);
        } else if (ex instanceof ImplicitProductTerm) {
            returnTerm = addWithoutChecks((ImplicitProductTerm) ex);
        } else {
            throw new ExpressionException();
        }
        return returnTerm;
    }

    @Override
    public Term multiplyValue(Term ex) {
        return null;
    }

    void processVariablesFromString(final String input, List<Expression> data) {
        final Matcher matcher = Pattern.compile("([A-Z][a-z]*)").matcher(input);
        while (matcher.find()) {
            data.add(new VariableTerm(matcher.group(0)));
        }
    }

    void processNumberTermFromString(final String input, List<Expression> data) {
        final Matcher matcher = Pattern.compile("([-]*[0-9]+([.][0-9]+)?)").matcher(input);
        if (matcher.find()) {
            final String numberSubstring = matcher.group(0);
            if (numberSubstring.indexOf('.') > 0) {
                data.add(0, new DecimalTerm(Double.parseDouble(numberSubstring)));
            } else {
                data.add(0, new IntegerTerm(Integer.parseInt(numberSubstring)));
            }
        } else {
            data.add(0, new IntegerTerm(1));
        }
    }

    Term addWithoutChecks(VariableTerm ex) {
        Term returnTerm = null;
        if (getData().get(0) instanceof IntegerTerm) {
            final int newCoefficient = getIntegerCoefficientValue() + 1;
            returnTerm = new ImplicitProductTerm("" + newCoefficient + ex.getData());
        } else {
            final BigDecimal starter = new BigDecimal(getNumericString());
            final BigDecimal possiblyNegativeValue = starter.multiply(new BigDecimal(getNegativeMultiplier()));
            final BigDecimal result = possiblyNegativeValue.add(new BigDecimal(1));
            returnTerm = new ImplicitProductTerm("" + result.toString() + ex.getData());
        }
        return returnTerm;
    }

    Term addWithoutChecks(ImplicitProductTerm otherTerm) {
        checkForVariableMismatch(otherTerm);
        return generateSummedTerm(otherTerm);
    }

    private int getIntegerCoefficientValue() {
        return ((IntegerTerm) getData().get(0)).getData() * getNegativeMultiplier();
    }

    boolean checkForAddableVariableTerm(Term ex) {
        return ex instanceof VariableTerm
                && getData().size() == 2
                && getData().get(1).toString().equals(ex.getData());
    }

    private Term generateSummedTerm(ImplicitProductTerm otherTerm) {
        final Term term = buildNumberPart(otherTerm);
        Term returnTerm = null;
        if (Double.parseDouble(term.toString()) == 0) {
            returnTerm = new IntegerTerm(0);
        } else {
            returnTerm = new ImplicitProductTerm(buildString(otherTerm, term));
        }
        return returnTerm;
    }

    private void checkForVariableMismatch(ImplicitProductTerm otherTerm) {
        if (!(termCountMatches(otherTerm) && termsMatch(otherTerm))) {
            throw new ExpressionException();
        }
    }

    private Term buildNumberPart(ImplicitProductTerm otherTerm) {
        Term term = null;
        if (getData().get(0) instanceof IntegerTerm && otherTerm.getData().get(0) instanceof IntegerTerm) {
            term = buildIntegerCoefficient(otherTerm);
        } else {
            term = buildDecimalCoefficient(otherTerm);
        }
        return term;
    }

    IntegerTerm buildIntegerCoefficient(ImplicitProductTerm otherTerm) {
        final int thisTermValue = getIntegerCoefficientValue();
        final int otherTermValue = otherTerm.getIntegerCoefficientValue();
        return new IntegerTerm(thisTermValue + otherTermValue);
    }

    DecimalTerm buildDecimalCoefficient(ImplicitProductTerm otherTerm) {
        final BigDecimal thisDecimal = new BigDecimal(getNumericCoefficientWithSign());
        final BigDecimal otherDecimal = new BigDecimal(otherTerm.getNumericCoefficientWithSign());
        final BigDecimal result = thisDecimal.add(otherDecimal);
        return new DecimalTerm(result.toString());
    }

    private String getNumericCoefficientWithSign() {
        return getNegativeSignIfNecessary() + getNumericString();
    }

    private String getNumericString() {
        return getData().get(0).toString();
    }

    private String getNegativeSignIfNecessary() {
        return getNegativeMultiplier() > 0 ? EMPTY_STRING : MINUS_SIGN;
    }

    String buildString(Term ex, Term newCoefficient) {
        final StringBuilder builder = new StringBuilder();
        builder.append(newCoefficient.toString());
        Expression part = null;
        for (int i = 1; i < getData().size(); i++) {
            part = getData().get(i);
            builder.append(part.toString());
        }
        return builder.toString();
    }

    private boolean termsMatch(ImplicitProductTerm otherTerm) {
        boolean returnValue = true;
        Expression expression2 = null;
        LOOP1:
        for (int i = 1; i < getData().size(); i++) {
            final Expression expression = getData().get(i);
            for (int j = 1; j < otherTerm.getData().size(); j++) {
                expression2 = otherTerm.getData().get(i);
                if (expression.toString().equals(expression2.toString())) {
                    continue LOOP1;
                }
            }
            returnValue = false;
            break;
        }
        return returnValue;
    }

    private boolean termCountMatches(ImplicitProductTerm otherTerm) {
        return getData().size() == otherTerm.getData().size();
    }

    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < negativeCount; i++) {
            builder.append(MINUS_SIGN);
        }
        for (Expression term : getData()) {
            if (!("1".equals(term.toString()))) {
                builder.append(term.toString());
            }
        }
        return builder.toString();
    }
}
