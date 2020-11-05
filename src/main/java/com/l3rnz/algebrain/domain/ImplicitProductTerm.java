package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImplicitProductTerm extends Term<List<Expression>> {

    public static final String IMPLICIT_PRODUCT_TERM_REGEX = "^[-]*(([0-9]+([.][0-9]+)?)|([A-Z][a-z]*))([A-Z][a-z]*)+$";

    public ImplicitProductTerm(final String input) {
        super(input);
    }

    public ImplicitProductTerm(Product inputProduct) {
        List<Expression> variables = new ArrayList<>();
        for (IndefiniteSizeExpressionPart term : inputProduct.terms) {
            ProductPart pPart = (ProductPart) term;
            Expression expression = pPart.getContainedExpression();
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
        for (Expression part: getData()) {
            if (!(part instanceof Term)) {
                continue;
            }
            term = (Term) (part);
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
        List<Expression> data = new ArrayList<>();
        processNumberTermFromString(input, data);
        processVariablesFromString(input, data);
        return data;
    }

    void processVariablesFromString(final String input, List<Expression> data) {
        Matcher matcher = Pattern.compile("([A-Z][a-z]*)").matcher(input);
        while (matcher.find()) {
            data.add(new VariableTerm(matcher.group(0)));
        }
    }

    void processNumberTermFromString(final String input, List<Expression> data) {
        Matcher matcher = Pattern.compile("([-]*[0-9]+([.][0-9]+)?)").matcher(input);
        if (matcher.find()) {
            String numberSubstring = matcher.group(0);
            if (numberSubstring.indexOf('.') > 0) {
                data.add(0,new DecimalTerm(Double.parseDouble(numberSubstring)));
            } else {
                data.add(0,new IntegerTerm(Integer.parseInt(numberSubstring)));
            }
        } else {
            data.add(0,new IntegerTerm(1));
        }
    }

    @Override
    public Term addValue(Term ex) {
        Term returnTerm = null;
        if (checkForAddableVariableTerm(ex)) {
            VariableTerm variableTerm = (VariableTerm) ex;
            returnTerm = addWithoutChecks(variableTerm);
        } else if (ex instanceof ImplicitProductTerm) {
            ImplicitProductTerm otherTerm = (ImplicitProductTerm) ex;
            returnTerm = addWithoutChecks(otherTerm);
        } else {
            throw new ExpressionException();
        }
        return returnTerm;
    }

    Term addWithoutChecks(VariableTerm ex) {
        Term returnTerm;
        if (getData().get(0) instanceof IntegerTerm) {
            int newCoefficient = ((IntegerTerm) getData().get(0)).getData() * getNegativeMultiplier() + 1;
            returnTerm = new ImplicitProductTerm("" + newCoefficient + ex.getData());
        } else {
            BigDecimal coeffCal = new BigDecimal(getData().get(0).toString()).multiply(new BigDecimal(getNegativeMultiplier()));
            BigDecimal result = coeffCal.add(new BigDecimal(1));
            returnTerm = new ImplicitProductTerm("" + result.toString() + ex.getData());
        }
        return returnTerm;
    }

    boolean checkForAddableVariableTerm(Term ex) {
        return ex instanceof VariableTerm
                && getData().size() == 2
                && getData().get(1).toString().equals((ex.getData()));
    }

    Term addWithoutChecks(ImplicitProductTerm otherTerm) {
        Term returnTerm = null;
        if (!allVariablesMatch(otherTerm)) {
            throw new ExpressionException();
        }
        Term term = buildNumberPart(otherTerm);
        if (Double.parseDouble(term.toString()) == 0) {
            returnTerm = new IntegerTerm(0);
        } else {
            returnTerm = new ImplicitProductTerm(buildString(otherTerm, term));
        }
        return returnTerm;
    }

    private Term buildNumberPart(ImplicitProductTerm otherTerm) {
        Term term = null;
        if (getData().get(0) instanceof IntegerTerm && otherTerm.getData().get(0)instanceof IntegerTerm) {
            term = buildIntegerCoefficient(otherTerm);
        } else {
            term = buildDecimalCoefficient(otherTerm);
        }
        return term;
    }

    IntegerTerm buildIntegerCoefficient(ImplicitProductTerm otherTerm) {

        int thisTermValue = ((IntegerTerm) getData().get(0)).getData() * getNegativeMultiplier();
        int otherTermValue = ((IntegerTerm) otherTerm.getData().get(0)).getData() * otherTerm.getNegativeMultiplier();
        return new IntegerTerm(thisTermValue + otherTermValue);
    }

    DecimalTerm buildDecimalCoefficient(ImplicitProductTerm otherTerm) {
        BigDecimal thisDecimal = new BigDecimal((getNegativeMultiplier() > 0 ? "" : "-") + getData().get(0).toString());
        BigDecimal otherDecimal = new BigDecimal((otherTerm.getNegativeMultiplier() > 0 ? "" : "-") + otherTerm.getData().get(0).toString());
        BigDecimal result = thisDecimal.add(otherDecimal);
        String output = result.toString();
        return new DecimalTerm(output);
    }

    String buildString(Term ex, Term newCoefficient) {
        StringBuilder builder = new StringBuilder();
        builder.append(newCoefficient.toString());
        for (int i = 1; i< getData().size(); i++) {
            Expression part = getData().get(i);
            builder.append(part.toString());
        }
        return builder.toString();
    }

    private boolean allVariablesMatch(ImplicitProductTerm otherTerm) {
        if (getData().size() != otherTerm.getData().size()) {
            return false;
        }
        LOOP1:
        for (int i = 1; i< getData().size(); i++) {
            Expression expression =getData().get(i);
            for (int j = 1; j< otherTerm.getData().size(); j++) {
                Expression expression2 =otherTerm.getData().get(i);
                if (expression.toString().equals(expression2.toString())) {
                    continue LOOP1;
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public Term multiplyValue(Term ex) {
        return null;
    }


    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < negativeCount; i++) {
            builder.append("-");
        }
        for (Expression term : getData()) {
            if (!("1".equals(term.toString()))) {
                builder.append(term.toString());
            }
        }
        return builder.toString();
    }
}
