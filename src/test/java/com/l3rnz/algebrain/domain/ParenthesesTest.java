package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParenthesesTest {

    @Test
    public void testThatParnetheticalExpressionWorks() {
        Term term = new IntegerTerm(3);
        String expected = "(3)";

        ParentheticalTerm pTerm = new ParentheticalTerm(term);
        String actual = pTerm.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testThatParentheticalExpressionWorksWithSum() {
        Term term = new IntegerTerm(3);
        Term term1 = new VariableTerm("X");
        Sum sum = new Sum();
        sum.addExpression(term);
        sum.addExpression(term1);
        String expected = "(3+X)";

        ParentheticalTerm pTerm = new ParentheticalTerm(sum);
        String actual = pTerm.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testThatParentheticalExpressionWorksToPassInString() {
        String expected = "(3)";
        ParentheticalTerm term = new ParentheticalTerm(expected);
        String actual = term.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testThatParentheticalExpressionFailsWithoutParentheses() {
        String expected = "3";
        assertThrows(ExpressionException.class, () -> {
            new ParentheticalTerm(expected);
        });
    }

    @Test
    public void testThatParentheticalExpressionHandlesNestedParentheses() {
        String expected = "(3+(4+5))";
        ParentheticalTerm term = new ParentheticalTerm(expected);
        String actual = term.toString();
        assertEquals(expected, actual);
    }

}
