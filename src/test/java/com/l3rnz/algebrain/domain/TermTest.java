package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;
import org.junit.jupiter.api.Test;

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
    public void testThatGetValueShowsRightValue() {
        IntegerTerm term = new IntegerTerm(-1);
        int actual = term.getValue();
        int expected = -1;
        assertEquals(expected, actual);
    }

    @Test
    public void testThatGetValueShowsRightValueWithSeveralNegatives() {
        IntegerTerm term = new IntegerTerm(-1);
        term.addNegative();
        term.addNegative();
        term.addNegative();
        int actual = term.getValue();
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
        double actual = term.getValue();
        double expected = 1.5;
        assertEquals(expected, actual);
        String actualText = term.toString();
        String expectedText = "----1.5";
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testVariableTermWorks() {
        String input = "x";
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
}
