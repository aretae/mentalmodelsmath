package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Tests Sum.
 */
public class SumTest {

    IntegerTerm term;
    IntegerTerm term2;
    IntegerTerm term3;
    IntegerTerm term4;
    IntegerTerm term5;

    DecimalTerm dt1;
    DecimalTerm dt2;
    DecimalTerm dt3;
    Sum sum3;

    Sum sum;
    Sum sum2;


    @BeforeEach
    public void setUp() {
        sum = new Sum();
        sum2 = new Sum();
        term = new IntegerTerm(3);
        term2 = new IntegerTerm(4);
        term3 = new IntegerTerm(5);
        term4 = new IntegerTerm(6);
        term5 = new IntegerTerm(-4);

        sum.addTerm(term);
        sum.addTerm(term2);

        sum2.addTerm(term);
        sum2.addTerm(term5);

    }



    @Test
    public void testThatSumDisplaysWell() {
        String actual = sum.toString();
        String expected = "3+4";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatSumDisplaysThree() {
        sum.addTerm(term3);
        String actual = sum.toString();
        String expected = "3+4+5";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatSumDisplaysNegative() {
        String actual = sum2.toString();
        String expected = "3-4";
        assertEquals(expected, actual);
    }


    @Test
    public void testThatSumCanDisplayPlusMinus() {
        IntegerTerm term3 = new IntegerTerm(-5);
        sum = new Sum();
        sum.addTerm(term);
        sum.addTerm(term5, true);
        sum.addTerm(term3, false);
        String actual = sum.toString();
        String expected = "3+-4-5";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatSumDisplaysDoubleNegative() {
        term5.addNegative();
        sum = new Sum();
        sum.addTerm(term);
        sum.addTerm(term5);
        String actual = sum.toString();
        String expected = "3--4";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatWeCanIdentifyElementInSum() {
        Expression ex = sum.findElementAt(0);
        String actual = ex.toString();
        String expected = "3";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatWeCanIdentifyEndElementInSum() {
        term5.addNegative();
        sum = new Sum();
        sum.addTerm(term);
        sum.addTerm(term5);
        Expression ex = sum.findElementAt(2);
        String actual = ex.toString();
        String expected = "--4";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatFindElementAtThrowsExceptionAsNeeded() {
        assertThrows(ExpressionException.class, () -> {
            Expression ex = sum.findElementAt(-1);
        });
    }

    @Test
    public void testThatFindElementAtThrowsExceptionTooBig() {
        assertThrows(ExpressionException.class, () -> {
            Expression ex = sum.findElementAt(17);
        });
    }

    @Test
    public void testThatContainsFindsElement() {
        Term e = sum.findElementAt(0);
        assertTrue(sum.contains(e));
    }


    @Test
    public void testThatContainsDoesntFindDuplicate() {
        IntegerTerm e = new IntegerTerm(3);
        assertFalse(sum.contains(e));
    }

    @Test
    public void testThatMoveWorks() {
        Term e = sum.findElementAt(0);
        sum.move(e, 2);
        String actual = sum.toString();
        String expected = "4+3";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatMoveWorksRightToLeftAndWithNegatives() {
        sum = new Sum();
        term5.addNegative();
        sum.addTerm(term);
        sum.addTerm(term5);
        Term e = sum.findElementAt(3);
        sum.move(e, 0);
        String actual = sum.toString();
        String expected = "--4+3";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatAddSumToSumMakesOneSum() {
        sum2 = new Sum();
        sum2.addTerm(term3);
        sum2.addTerm(term4);

        sum.addTerm(sum2);
        String actual = sum.toString();
        String expected = "3+4+5+6";
        assertEquals(expected, actual);
    }


    @Test
    public void testThatMergeWorks() {

        Term e = sum.findElementAt(0);
        Expression newEx = sum.merge(e, false);
        String actual = newEx.toString();
        String expected = "7";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatParseIntWorksOnDoubleNegative() {
        String testCase = "-3";
        assertEquals(-3, Integer.parseInt(testCase));

    }

    @Test
    public void testThatFindElementFailsWhenPastEnd() {

        assertThrows(ExpressionException.class, () -> {
            sum.findElementAt(42);
        });
    }

    @Test
    public void testThatFindElementFailsWhenExactSize() {

        assertThrows(ExpressionException.class, () -> {
            sum.findElementAt(3);
        });
    }

    @Test
    public void testThatFindIndexOfWorksPastZero() {
        int expected = 1;
        int actual = sum.findIndexOf(term2);
        assertEquals(expected, actual);
    }

    @Test
    public void testThatMoveThrowsExceptionIfWrongExpression() {
        assertThrows(ExpressionException.class, () -> {
            sum.move(term3, 0);
        });
    }

    @Test
    public void testThatMergeNonExistentTermThrowsException() {
        assertThrows(ExpressionException.class, () -> {
            sum.merge(term3, false);
        });
    }

    @Test
    public void testThatMergeOffLeftSideThrowsException() {
        assertThrows(ExpressionException.class, () -> {
            sum.merge(term, true);
        });
    }

    @Test
    public void testThatMergeOffRightSideThrowsException() {
        assertThrows(ExpressionException.class, () -> {
            sum.merge(term2, false);
        });
    }

    @Test
    public void testThatFindElementAtSizeFails() {
        assertThrows(ExpressionException.class, () -> {
            sum.findElementAt(2);
        });
    }


    @Test
    public void testThatItAllWorksWithDecimalToo() {
        DecimalTerm dt1 = new DecimalTerm(-3.2);
        DecimalTerm dt3 = new DecimalTerm(1.5);
        Sum sum3 = new Sum();

        dt1.addNegative();
        sum3.addTerm(dt1);
        sum3.addTerm(term);
        sum3.addTerm(dt3);
        Term e = sum3.findElementAt(5);
        sum3.move(e, 0);
        String actual = sum3.toString();
        String expected = "3--3.2+1.5";
        assertEquals(expected, actual);
    }


    @Test
    public void testThatMergeWorksWithDecimal() {
        DecimalTerm dt1 = new DecimalTerm(-3.2);
        Sum sum3 = new Sum();

        dt1.addNegative();
        sum3.addTerm(dt1);
        sum3.addTerm(term);

        Term e = sum3.findElementAt(0);
        Expression newEx = sum3.merge(e, false);
        String actual = newEx.toString();
        String expected = "6.2";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatMergeWorksWithTwoDecimals() {
        DecimalTerm dt1 = new DecimalTerm(-3.2);
        DecimalTerm dt2 = new DecimalTerm(8.8);
        Sum sum3 = new Sum();

        sum3.addTerm(dt1);
        sum3.addTerm(dt2);

        Term e = sum3.findElementAt(0);
        Expression newEx = sum3.merge(e, false);
        String actual = newEx.toString();
        String expected = "5.6";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatMergeWorksWithDecimalBackwards() {
        DecimalTerm dt1 = new DecimalTerm(3.2);
        Sum sum3 = new Sum();

        sum3.addTerm(term);
        sum3.addTerm(dt1);

        Term e = sum3.findElementAt(0);
        Expression newEx = sum3.merge(e, false);
        String actual = newEx.toString();
        String expected = "6.2";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatIntTermDoesntMergeWithVariableTerm() {
        VariableTerm vt1 = new VariableTerm("x");
        DecimalTerm dt1 = new DecimalTerm(3.2);
        Sum sum3 = new Sum();

        sum3.addTerm(dt1);
        sum3.addTerm(vt1);

        Term e = sum3.findElementAt(0);
        assertThrows( ExpressionException.class, () -> {
            sum3.merge(e, false);
        });
    }

    @Test
    public void testThatDecimalTermDoesntMergeWithVariableTerm() {
        VariableTerm vt1 = new VariableTerm("x");
        Sum sum3 = new Sum();

        sum3.addTerm(term);
        sum3.addTerm(vt1);

        Term e = sum3.findElementAt(0);
        assertThrows( ExpressionException.class, () -> {
            sum3.merge(e, false);
        });
    }

}
