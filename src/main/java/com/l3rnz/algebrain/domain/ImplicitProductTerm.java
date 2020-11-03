package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImplicitProductTerm extends Term<String> {

    public static final String IMPLICIT_PRODUCT_TERM_REGEX = "^[-]*(([0-9]+([.][0-9]+)?)|([A-Z][a-z]*))([A-Z][a-z]*)+$";
    private NumericTerm coefficient;
    private List<ProductPart> variables;

    public ImplicitProductTerm(String input) {
        variables = new ArrayList<>();
        checkDataValidity(input);
        setNegatives(input);
        setData(input);
        processNegatives();
    }

    public ImplicitProductTerm(Product inputProduct) {
        variables = new ArrayList<>();
        for (IndefiniteSizeExpressionPart term : inputProduct.terms) {
            ProductPart pPart = (ProductPart) term;
            if (term.getContainedExpression() instanceof NumericTerm) {
                coefficient = (NumericTerm) pPart.getContainedExpression();
            } else {
                variables.add(pPart);
            }
        }
        if (coefficient == null) {
            coefficient = new IntegerTerm(1);
        }
        processNegatives();
    }

    private void processNegatives() {
        boolean swapNegative = countAndStripNegatives();
        if (swapNegative) {
            coefficient.addNegative();
        }
    }

    private boolean countAndStripNegatives() {
        boolean swapNegative = false;
        Term term = null;
        for (ProductPart part : variables) {
            term = (Term) (part.getContainedExpression());
            if (term.getNegativeCount() % 2 == 1) {
                swapNegative = !swapNegative;
            }
            term.resetNegatives();
        }
        if (coefficient.getNegativeCount() % 2 == 1) {
            swapNegative = !swapNegative;
        }
        coefficient.resetNegatives();
        return swapNegative;
    }

    @Override
    public void checkDataValidity(String input) {
        if (!input.matches(IMPLICIT_PRODUCT_TERM_REGEX)) {
            throw new ExpressionException();
        }
    }

    @Override
    public String convertToType(String data) {
        return null;
    }

    public void setData(final String data) {
        processNumberTermFromString(data);
        processVariablesFromString(data);
    }

    void processVariablesFromString(final String data) {
        Matcher matcher = Pattern.compile("([A-Z][a-z]*)").matcher(data);
        while (matcher.find()) {
            variables.add(new ProductPart(new VariableTerm(matcher.group(0))));
        }
    }

    void processNumberTermFromString(final String data) {
        Matcher matcher = Pattern.compile("([-]*[0-9]+([.][0-9]+)?)").matcher(data);
        if (matcher.find()) {
            String numberSubstring = matcher.group(0);
            if (numberSubstring.indexOf('.') > 0) {
                coefficient = new DecimalTerm(Double.parseDouble(numberSubstring));
            } else {
                coefficient = new IntegerTerm(Integer.parseInt(numberSubstring));
            }
        } else {
            coefficient = new IntegerTerm(1);
        }
    }

    @Override
    public Term addValue(Term ex) {
        Term returnTerm = null;
        if (checkAddability(ex)) {
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
        if (coefficient instanceof IntegerTerm) {
            int newCoefficient = ((IntegerTerm) coefficient).getValue() + 1;
            returnTerm = new ImplicitProductTerm("" + newCoefficient + ex.getData());
        } else {
            BigDecimal coeffCal = new BigDecimal(coefficient.toString());
            BigDecimal result = coeffCal.add(new BigDecimal(1));
            returnTerm = new ImplicitProductTerm("" + result.toString() + ex.getData());
        }
        return returnTerm;
    }

    boolean checkAddability(Term ex) {
        return ex instanceof VariableTerm
                && variables.size() == 1
                && variables.get(0).getContainedExpression().toString().equals((ex.getData()));
    }

    Term addWithoutChecks(ImplicitProductTerm otherTerm) {
        Term returnTerm = null;
        if (!allVariablesMatch(otherTerm)) {
            throw new ExpressionException();
        }
        Term term = buildNumberPart(otherTerm);
        if (Double.parseDouble(term.toString())==0) {
            returnTerm = new IntegerTerm(0);
        } else {
            returnTerm = new ImplicitProductTerm(buildString(otherTerm, term));
        }
        return returnTerm;
    }

    private Term buildNumberPart(ImplicitProductTerm otherTerm) {
        Term term = null;
        if (coefficient instanceof IntegerTerm && otherTerm.coefficient instanceof IntegerTerm) {
            term = buildIntegerCoefficient(otherTerm);
        } else {
            term = buildDecimalCoefficient(otherTerm);
        }
        return term;
    }

    IntegerTerm buildIntegerCoefficient(ImplicitProductTerm otherTerm) {
        return new IntegerTerm(addIntegerCoefficients(otherTerm));
    }

    DecimalTerm buildDecimalCoefficient(ImplicitProductTerm otherTerm) {
        BigDecimal coeffCal = new BigDecimal(coefficient.toString());
        BigDecimal result = coeffCal.add(new BigDecimal(otherTerm.coefficient.toString()));
        String output = result.toString();
        return new DecimalTerm(output);
    }

    String buildString(Term ex, Term newCoefficient) {
        StringBuilder builder = new StringBuilder();
        builder.append(newCoefficient.toString());
        for (ProductPart part : variables) {
            builder.append(part.getContainedExpression().toString());
        }
        return builder.toString();
    }

    int addIntegerCoefficients(ImplicitProductTerm otherTerm) {
        return ((IntegerTerm) coefficient).getValue() + ((IntegerTerm) otherTerm.coefficient).getValue();
    }

    private boolean allVariablesMatch(ImplicitProductTerm otherTerm) {
        if (variables.size() != otherTerm.variables.size()) {
            return false;
        }
        LOOP1:
        for (ProductPart part : variables) {
            for (ProductPart part2 : otherTerm.variables) {
                if (part.toString().equals(part2.toString())) {
                    continue LOOP1;
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public String getValue() {
        return getData();
    }

    @Override
    public Term multiplyValue(Term ex) {
        return null;
    }




    public String toString() {
        StringBuilder builder = new StringBuilder();
        addCoefficientProperly(builder);
        for (ProductPart term : variables) {
            builder.append(term.toString());
        }
        return builder.toString();
    }

    void addCoefficientProperly(StringBuilder builder) {
        if (("-1").equals(coefficient.toString())) {
            builder.append("-");
        } else if (!"1".equals(coefficient.toString())) {
            builder.append(coefficient.toString());
        }
    }
}
