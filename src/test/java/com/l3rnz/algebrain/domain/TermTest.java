package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests IntegerTerm and DecimalTerm.
 */
public class TermTest {

    @Test
    public void testThatTermDisplaysWell() {
        IntegerTerm term = new IntegerTerm(3);
        String actual = term.toString();
        String expected = "3";
        assertEquals(expected, actual);
    }


    @Test
    public void testThatZeroIsNotNegative() {
        IntegerTerm term = new IntegerTerm(0);
        String actual = term.toString();
        String expected = "0";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatGetValueShowsRightValueWithSeveralNegatives() {
        IntegerTerm term = new IntegerTerm(-1);
        term.addNegative();
        term.addNegative();
        term.addNegative();
        int actual = term.getData();
        int expected = 1;
        assertEquals(expected, actual);
        String actualText = term.toString();
        String expectedText = "----1";
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testThatDecimalTermDisplaysWell() {
        DecimalTerm term = new DecimalTerm(3.2);
        String actual = term.toString();
        String expected = "3.2";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatGetValueOnDecimalShowsRightValueWithSeveralNegatives() {
        DecimalTerm term = new DecimalTerm(-1.5);
        term.addNegative();
        term.addNegative();
        term.addNegative();
        double actual = term.getData();
        double expected = 1.5;
        assertEquals(expected, actual);
        String actualText = term.toString();
        String expectedText = "----1.5";
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testVariableTermWorks() {
        String input = "X";
        VariableTerm term = new VariableTerm(input);
        String actual = term.toString();
        assertEquals(input, actual);
    }

    @Test
    public void testThatVariableTermMustBeAllLetters() {
        assertThrows(ExpressionException.class, () -> {
            new VariableTerm("A1");
        });
    }

    @Test
    public void testNegativeVariableTermWorks() {
        String input = "-Bob";
        VariableTerm term = new VariableTerm(input);
        String actual = term.toString();
        assertEquals(input, actual);
    }

    @Test
    public void testNegativeVariableTermIsNegative() {
        String input = "-Bob";
        VariableTerm term = new VariableTerm(input);
        boolean actual = term.isNegative();
        assertTrue(actual);
    }

    @Test
    public void testVariableTermWithThreeNegatives() {
        String input = "---Bob";
        VariableTerm term = new VariableTerm(input);
        boolean actual = term.isNegative();
        assertTrue(actual);
        assertEquals(3, term.getNegativeCount());
    }

    @Test
    public void testThatVariableTermThrowsExceptionWithBadCaps() {
        assertThrows(ExpressionException.class, () -> {
            new VariableTerm("xyz");
        });
    }

    @ParameterizedTest
    @ValueSource(strings= {"3X", "3Abba", "3XY", "2.2B", "3.14PiAreSquared"})
    public void testThatImplicitProductTermWorks(String input) {
        ImplicitProductTerm term = new ImplicitProductTerm(input);
        String actual = term.toString();
        assertEquals(input, actual);
    }

    @ParameterizedTest
    @ValueSource(strings= {"X", "X2", "3x", "3X3", "3xAba", "3.1.1A", ".11Abc"})
    public void testThatImplicitProductOnlyAllowsNumbersFirst(String data) {
        assertThrows(ExpressionException.class, () -> {
           new ImplicitProductTerm(data);
        }, data);
    }

    @Test
    public void testThatSimpleProductConvertsToImplicitProduct() {
        String expected = "3X";

        Product product = new Product();
        Expression expression = new IntegerTerm(3);
        Expression expression1 = new VariableTerm("X");
        product.addExpression(expression);
        product.addExpression(expression1);

        ImplicitProductTerm term = new ImplicitProductTerm(product);
        String actual = term.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testThatNegativeSimpleProductConvertsToImplicitProduct() {
        String expected = "-3X";

        Product product = new Product();
        Expression expression = new IntegerTerm(-3);
        Expression expression1 = new VariableTerm("X");
        product.addExpression(expression);
        product.addExpression(expression1);

        ImplicitProductTerm term = new ImplicitProductTerm(product);
        String actual = term.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testThatBackwardsNegativeProductConvertsToImplicitProduct() {
        String expected = "-3X";

        Product product = new Product();
        Expression expression = new IntegerTerm(3);
        Expression expression1 = new VariableTerm("-X");
        product.addExpression(expression);
        product.addExpression(expression1);

        ImplicitProductTerm term = new ImplicitProductTerm(product);
        String actual = term.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testThatComplexNegativesWorkWithImplicitProduct() {
        String expected = "3XY";

        Product product = new Product();
        Expression expression = new IntegerTerm(-3);
        Expression expression1 = new VariableTerm("---X");
        Expression expression2 = new VariableTerm("--Y");
        product.addExpression(expression);
        product.addExpression(expression1);
        product.addExpression(expression2);

        ImplicitProductTerm term = new ImplicitProductTerm(product);
        String actual = term.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testThatIntegerTermWorkswithGoodString() {
        String expected = "3";
        IntegerTerm term = new IntegerTerm(expected);
        String actual = term.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testThatDecimalTermWorkswithGoodString() {
        String expected = "3.3";
        DecimalTerm term = new DecimalTerm(expected);
        String actual = term.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testThatIntegerTermWorkswithGoodNegativeString() {
        String expected = "-3";
        IntegerTerm term = new IntegerTerm(expected);
        String actual = term.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testThatDecimalTermWorkswithGoodNegativeString() {
        String expected = "-3.3";
        DecimalTerm term = new DecimalTerm(expected);
        String actual = term.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testThatIntegerTermWorkswithDoubleNegativeString() {
        String expected = "--3";
        IntegerTerm term = new IntegerTerm(expected);
        String actual = term.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testThatDecimalTermWorkswithMultipleNegativeString() {
        String expected = "---3.3";
        DecimalTerm term = new DecimalTerm(expected);
        String actual = term.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testThatImplicitVariableTermWorkswithMultipleNegativeString() {
        String expected = "---3.3";
        DecimalTerm term = new DecimalTerm(expected);
        String actual = term.toString();
        assertEquals(expected, actual);
    }
}
