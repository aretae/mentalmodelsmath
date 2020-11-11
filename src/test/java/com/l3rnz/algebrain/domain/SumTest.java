package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

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

        sum.addExpression(term);
        sum.addExpression(term2);

        sum2.addExpression(term);
        sum2.addExpression(term5);

    }



    @Test
    public void testThatSumDisplaysWell() {
        String actual = sum.toString();
        String expected = "3+4";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatSumDisplaysThree() {
        sum.addExpression(term3);
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
        sum.addExpression(term);
        sum.addExpression(term5, true);
        sum.addExpression(term3, false);
        String actual = sum.toString();
        String expected = "3+-4-5";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatSumDisplaysDoubleNegative() {
        term5.addNegative();
        sum = new Sum();
        sum.addExpression(term);
        sum.addExpression(term5);
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
        sum.addExpression(term);
        sum.addExpression(term5);
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
        Expression e = sum.findElementAt(0);
        assertTrue(sum.contains(e));
    }


    @Test
    public void testThatContainsDoesntFindDuplicate() {
        IntegerTerm e = new IntegerTerm(3);
        assertFalse(sum.contains(e));
    }

    @Test
    public void testThatMoveWorks() {
        Expression e = sum.findElementAt(0);
        sum.move(e, 2);
        String actual = sum.toString();
        String expected = "4+3";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatMoveWorksRightToLeftAndWithNegatives() {
        sum = new Sum();
        term5.addNegative();
        sum.addExpression(term);
        sum.addExpression(term5);
        Expression e = sum.findElementAt(3);
        sum.move(e, 0);
        String actual = sum.toString();
        String expected = "--4+3";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatAddSumToSumMakesOneSum() {
        sum2 = new Sum();
        sum2.addExpression(term3);
        sum2.addExpression(term4);

        sum.addExpression(sum2);
        String actual = sum.toString();
        String expected = "3+4+5+6";
        assertEquals(expected, actual);
    }


    @Test
    public void testThatMergeWorks() {
        Expression e = sum.findElementAt(0);
        Expression newEx = sum.merge(e, false);
        String actual = newEx.toString();
        String expected = "7";
        assertEquals(expected, actual);
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
        sum3.addExpression(dt1);
        sum3.addExpression(term);
        sum3.addExpression(dt3);
        Expression e = sum3.findElementAt(5);
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
        sum3.addExpression(dt1);
        sum3.addExpression(term);

        Expression e = sum3.findElementAt(0);
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

        sum3.addExpression(dt1);
        sum3.addExpression(dt2);

        Expression e = sum3.findElementAt(0);
        Expression newEx = sum3.merge(e, false);
        String actual = newEx.toString();
        String expected = "5.6";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatMergeWorksWithDecimalBackwards() {
        DecimalTerm dt1 = new DecimalTerm(3.2);
        Sum sum3 = new Sum();

        sum3.addExpression(term);
        sum3.addExpression(dt1);

        Expression e = sum3.findElementAt(0);
        Expression newEx = sum3.merge(e, false);
        String actual = newEx.toString();
        String expected = "6.2";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatIntTermDoesntMergeWithVariableTerm() {
        VariableTerm vt1 = new VariableTerm("X");
        DecimalTerm dt1 = new DecimalTerm(3.2);
        Sum sum3 = new Sum();

        sum3.addExpression(dt1);
        sum3.addExpression(vt1);

        Expression e = sum3.findElementAt(0);
        assertThrows( ExpressionException.class, () -> {
            sum3.merge(e, false);
        });
    }

    @Test
    public void testThatDecimalTermDoesntMergeWithVariableTerm() {
        VariableTerm vt1 = new VariableTerm("X");
        Sum sum3 = new Sum();

        sum3.addExpression(term);
        sum3.addExpression(vt1);

        Expression e = sum3.findElementAt(0);
        assertThrows( ExpressionException.class, () -> {
            sum3.merge(e, false);
        });
    }

    @Test
    public void testThatNegativeVariableTermCancelsPositive() {
        VariableTerm vt1 = new VariableTerm("X");
        VariableTerm vt2 = new VariableTerm("-X");
        Sum sum3 = new Sum();

        sum3.addExpression(vt1);
        sum3.addExpression(vt2);

        Expression e = sum3.findElementAt(0);
        Expression newEx = sum3.merge(e, false);
        String actual = newEx.toString();
        String expected = "0";
        assertEquals(expected, actual);
    }


    @ParameterizedTest
    @MethodSource("terms")
    public void testThatVariableTermDoesntAddWithStuff(Term t) {
        VariableTerm vt1 = new VariableTerm("X");
        Sum sum3 = new Sum();

        sum3.addExpression(vt1);
        sum3.addExpression(t);

        Expression e = sum3.findElementAt(0);
        assertThrows( ExpressionException.class, () -> {
            sum3.merge(e, false);
        }, t.toString());
    }

    static Stream<Term> terms() {
        return Stream.of(new IntegerTerm(3), new DecimalTerm(3.3), new VariableTerm("Y"), new ImplicitProductTerm("2Y"), new ImplicitProductTerm("XY"));
    }

    @Test
    public void testThatVariablesAddCorrectly() {
        VariableTerm vt1 = new VariableTerm("X");
        VariableTerm vt2 = new VariableTerm("X");
        Sum sum3 = new Sum();

        sum3.addExpression(vt1);
        sum3.addExpression(vt2);

        Expression e = sum3.findElementAt(0);
        Expression newEx = sum3.merge(e, false);
        String actual = newEx.toString();
        String expected = "2X";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatNegativeVariablesAddCorrectly() {
        VariableTerm vt1 = new VariableTerm("-Fred");
        VariableTerm vt2 = new VariableTerm("-Fred");
        Sum sum3 = new Sum();

        sum3.addExpression(vt1);
        sum3.addExpression(vt2);

        Expression e = sum3.findElementAt(0);
        Expression newEx = sum3.merge(e, false);
        String actual = newEx.toString();
        String expected = "-2Fred";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatImplicitVariablesAddCorrectly() {
        VariableTerm vt1 = new VariableTerm("Fred");
        ImplicitProductTerm ipt2 = new ImplicitProductTerm("2Fred");
        Sum sum3 = new Sum();

        sum3.addExpression(vt1);
        sum3.addExpression(ipt2);

        Expression e = sum3.findElementAt(0);
        Expression newEx = sum3.merge(e, false);
        String actual = newEx.toString();
        String expected = "3Fred";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatImplicitVariableDoublesAddCorrectly() {
        VariableTerm vt1 = new VariableTerm("Pi");
        ImplicitProductTerm ipt2 = new ImplicitProductTerm("-4.14Pi");
        Sum sum3 = new Sum();

        sum3.addExpression(vt1);
        sum3.addExpression(ipt2);

        Expression e = sum3.findElementAt(0);
        Expression newEx = sum3.merge(e, false);
        String actual = newEx.toString();
        String expected = "-3.14Pi";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatImplicitVariablesAddWhenMatching() {
        ImplicitProductTerm ipt1 = new ImplicitProductTerm("2Y");
        ImplicitProductTerm ipt2 = new ImplicitProductTerm("-4Y");
        Sum sum3 = new Sum();

        sum3.addExpression(ipt1);
        sum3.addExpression(ipt2);

        Expression e = sum3.findElementAt(0);
        Expression newEx = sum3.merge(e, false);
        String actual = newEx.toString();
        String expected = "-2Y";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatImplicitVariablesAddDoublesWhenMatching() {
        ImplicitProductTerm ipt1 = new ImplicitProductTerm("2.3Y");
        ImplicitProductTerm ipt2 = new ImplicitProductTerm("-3.2Y");
        Sum sum3 = new Sum();

        sum3.addExpression(ipt1);
        sum3.addExpression(ipt2);

        Expression e = sum3.findElementAt(0);
        Expression newEx = sum3.merge(e, false);
        String actual = newEx.toString();
        String expected = "-0.9Y";
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"2XZ", "2X", "2XYZ"}) //
    public void testThatIncompatibleImplicitVariableTermsDontAdd(String s) {
        ImplicitProductTerm ipt1 = new ImplicitProductTerm("2XY");
        ImplicitProductTerm ipt2 = new ImplicitProductTerm(s);
        Sum sum3 = new Sum();

        sum3.addExpression(ipt1);
        sum3.addExpression(ipt2);

        Expression e = sum3.findElementAt(0);
        assertThrows( ExpressionException.class, () -> {
            sum3.merge(e, false);
        }, s);
    }

    @Test
    public void testThatImplicitVariablesAddDoubleVarsWhenMatching() {
        ImplicitProductTerm ipt1 = new ImplicitProductTerm("2XY");
        ImplicitProductTerm ipt2 = new ImplicitProductTerm("3XY");
        Sum sum3 = new Sum();

        sum3.addExpression(ipt1);
        sum3.addExpression(ipt2);

        Expression e = sum3.findElementAt(0);
        Expression newEx = sum3.merge(e, false);
        String actual = newEx.toString();
        String expected = "5XY";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatImplicitVariablesAddToZero() {
        ImplicitProductTerm ipt1 = new ImplicitProductTerm("2XY");
        ImplicitProductTerm ipt2 = new ImplicitProductTerm("-2XY");
        Sum sum3 = new Sum();

        sum3.addExpression(ipt1);
        sum3.addExpression(ipt2);

        Expression e = sum3.findElementAt(0);
        Expression newEx = sum3.merge(e, false);
        String actual = newEx.toString();
        String expected = "0";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatImplicitVariablesDoublesAddToZero() {
        ImplicitProductTerm ipt1 = new ImplicitProductTerm("2.2XY");
        ImplicitProductTerm ipt2 = new ImplicitProductTerm("-2.2XY");
        Sum sum3 = new Sum();

        sum3.addExpression(ipt1);
        sum3.addExpression(ipt2);

        Expression e = sum3.findElementAt(0);
        Expression newEx = sum3.merge(e, false);
        String actual = newEx.toString();
        String expected = "0";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatImplicitVariablesAddWithAndWithoutCoefficients() {
        ImplicitProductTerm ipt1 = new ImplicitProductTerm("XY");
        ImplicitProductTerm ipt2 = new ImplicitProductTerm("2XY");
        Sum sum3 = new Sum();

        sum3.addExpression(ipt1);
        sum3.addExpression(ipt2);

        Expression e = sum3.findElementAt(0);
        Expression newEx = sum3.merge(e, false);
        String actual = newEx.toString();
        String expected = "3XY";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatImplicitVariablesAddWithAndWithoutCoefficientsAndNegatives() {
        ImplicitProductTerm ipt1 = new ImplicitProductTerm("-XY");
        ImplicitProductTerm ipt2 = new ImplicitProductTerm("2XY");
        Sum sum3 = new Sum();

        sum3.addExpression(ipt1);
        sum3.addExpression(ipt2);

        Expression e = sum3.findElementAt(0);
        Expression newEx = sum3.merge(e, false);
        String actual = newEx.toString();
        String expected = "XY";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatSumSupportsParentheses() {
        IntegerTerm term = new IntegerTerm(3);
        IntegerTerm term1 = new IntegerTerm(4);
        ParentheticalTerm paren = new ParentheticalTerm(term1);
        String expected = "3+(4)";
        Sum sum = new Sum();
        sum.addExpression(term);
        sum.addExpression(paren);
        String actual = sum.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testThatAddParenthesesWithNumberInsideMakesSum() {
        IntegerTerm term = new IntegerTerm(3);
        IntegerTerm term1 = new IntegerTerm(4);
        ParentheticalTerm paren = new ParentheticalTerm(term1);
        String expected = "3+4";
        Sum sum = new Sum();
        sum.addExpression(term);
        sum.addExpression(paren);
        Expression e = sum.findElementAt(0);
        Expression newEx = sum3.merge(e, false);
        String actual = newEx.toString();
        assertEquals(expected, actual);
    }
}
