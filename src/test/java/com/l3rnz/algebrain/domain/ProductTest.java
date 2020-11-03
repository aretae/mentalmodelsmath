package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ProductTest {

    IntegerTerm term;
    IntegerTerm term2;
    IntegerTerm term3;
    IntegerTerm term4;
    IntegerTerm term5;

    DecimalTerm dt1;
    DecimalTerm dt2;
    DecimalTerm dt3;
    Sum sum3;

    Product product;
    Product product2;


    @BeforeEach
    public void setUp() {
        product = new Product();
        product2 = new Product();
        term = new IntegerTerm(3);
        term2 = new IntegerTerm(4);
        term3 = new IntegerTerm(5);
        term4 = new IntegerTerm(6);
        term5 = new IntegerTerm(-4);

        product.addExpression(term);
        product.addExpression(term2);

        product2.addExpression(term3);
        product2.addExpression(term4);

    }


    @Test
    public void testThatProductDisplaysWell() {
        String actual = product.toString();
        String expected = "3*4";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatProductDisplaysWellWithThreeTermsIncludingNegative() {
        product.addExpression(term5);
        String actual = product.toString();
        String expected = "3*4*-4";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatProductDisplaysDoubleNegative() {
        term5.addNegative();
        product = new Product();
        product.addExpression(term);
        product.addExpression(term5);
        String actual = product.toString();
        String expected = "3*--4";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatWeCanIdentifyElementInSum() {
        Expression ex = product.findElementAt(0);
        String actual = ex.toString();
        String expected = "3";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatWeCanIdentifyEndElementInSum() {
        term5.addNegative();
        product = new Product();
        product.addExpression(term);
        product.addExpression(term5);
        Expression ex = product.findElementAt(2);
        String actual = ex.toString();
        String expected = "--4";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatFindElementAtThrowsExceptionAsNeeded() {
        assertThrows(ExpressionException.class, () -> {
            Expression ex = product.findElementAt(-1);
        });
    }

    @Test
    public void testThatFindElementAtThrowsExceptionTooBig() {
        assertThrows(ExpressionException.class, () -> {
            Expression ex = product.findElementAt(17);
        });
    }

    @Test
    public void testThatContainsFindsElement() {
        Expression e = product.findElementAt(0);
        assertTrue(product.contains(e));
    }


    @Test
    public void testThatContainsDoesntFindDuplicate() {
        IntegerTerm e = new IntegerTerm(3);
        assertFalse(product.contains(e));
    }

    @Test
    public void testThatMoveWorks() {
        Expression e = product.findElementAt(0);
        product.move(e, 2);
        String actual = product.toString();
        String expected = "4*3";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatMoveWorksRightToLeftAndWithNegatives() {
        product = new Product();
        term5.addNegative();
        product.addExpression(term);
        product.addExpression(term5);
        Expression e = product.findElementAt(3);
        product.move(e, 0);
        String actual = product.toString();
        String expected = "--4*3";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatAddProductToProductMakesOneProduct() {
        product.addExpression(product2);
        String actual = product.toString();
        String expected = "3*4*5*6";
        assertEquals(expected, actual);
    }


    @Test
    public void testThatMergeWorks() {
        Expression e = product.findElementAt(0);
        Expression newEx = product.merge(e, false);
        String actual = newEx.toString();
        String expected = "12";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatFindElementFailsWhenPastEnd() {

        assertThrows(ExpressionException.class, () -> {
            product.findElementAt(42);
        });
    }

    @Test
    public void testThatFindElementFailsWhenExactSize() {
        assertThrows(ExpressionException.class, () -> {
            product.findElementAt(3);
        });
    }

    @Test
    public void testThatFindIndexOfWorksPastZero() {
        int expected = 1;
        int actual = product.findIndexOf(term2);
        assertEquals(expected, actual);
    }

    @Test
    public void testThatMoveThrowsExceptionIfWrongExpression() {
        assertThrows(ExpressionException.class, () -> {
            product.move(term3, 0);
        });
    }

    @Test
    public void testThatMergeNonExistentTermThrowsException() {
        assertThrows(ExpressionException.class, () -> {
            product.merge(term3, false);
        });
    }

    @Test
    public void testThatMergeOffLeftSideThrowsException() {
        assertThrows(ExpressionException.class, () -> {
            product.merge(term, true);
        });
    }

    @Test
    public void testThatMergeOffRightSideThrowsException() {
        assertThrows(ExpressionException.class, () -> {
            product.merge(term2, false);
        });
    }

    @Test
    public void testThatFindElementAtSizeFails() {
        assertThrows(ExpressionException.class, () -> {
            product.findElementAt(2);
        });
    }

    @Test
    public void testThatMergeWorksWithDecimal() {
        DecimalTerm dt1 = new DecimalTerm(-3.2);
        Product product3 = new Product();

        dt1.addNegative();
        product3.addExpression(dt1);
        product3.addExpression(term);

        Expression e = product3.findElementAt(0);
        Expression newEx = product3.merge(e, false);
        String actual = newEx.toString();
        String expected = "9.6";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatMergeWorksWithDecimalBackwards() {
        DecimalTerm dt1 = new DecimalTerm(3.2);
        Product product3 = new Product();

        product3.addExpression(term);
        product3.addExpression(dt1);

        Expression e = product3.findElementAt(0);
        Expression newEx = product3.merge(e, false);
        String actual = newEx.toString();
        String expected = "9.6";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatMergeWorksWithDecimalZeroAndVariable() {
        DecimalTerm dt1 = new DecimalTerm(0.0);
        VariableTerm vt1 = new VariableTerm("X");

        Product product3 = new Product();
        product3.addExpression(dt1);
        product3.addExpression(vt1);

        Expression e = product3.findElementAt(0);
        Expression newEx = product3.merge(e, false);
        String actual = newEx.toString();
        String expected = "0";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatYouCantAddSumToProduct() {
        Sum sum = new Sum();

        sum.addExpression(term3);
        sum.addExpression(term4);

        assertThrows(ExpressionException.class, () -> {
           product.addExpression(sum);
        });
    }

    @Test
    public void testThatMultiplyWorksWithNumAndVar() {
        DecimalTerm dt1 = new DecimalTerm(2.2);
        VariableTerm vt1 = new VariableTerm("X");

        Product product3 = new Product();
        product3.addExpression(dt1);
        product3.addExpression(vt1);

        Expression e = product3.findElementAt(0);
        Expression newEx = product3.merge(e, false);
        String actual = newEx.toString();
        String expected = "2.2X";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatMultiplyWorksWithNumAndVarBackwardsAndNegative() {
        DecimalTerm dt1 = new DecimalTerm(-2.2);
        VariableTerm vt1 = new VariableTerm("-X");

        Product product3 = new Product();
        product3.addExpression(vt1);
        product3.addExpression(dt1);

        Expression e = product3.findElementAt(0);
        Expression newEx = product3.merge(e, false);
        String actual = newEx.toString();
        String expected = "2.2X";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatMultiplyWorksWithTwoVars() {
        VariableTerm vt1 = new VariableTerm("X");
        VariableTerm vt2 = new VariableTerm("Y");

        Product product3 = new Product();
        product3.addExpression(vt1);
        product3.addExpression(vt2);

        Expression e = product3.findElementAt(0);
        Expression newEx = product3.merge(e, false);
        String actual = newEx.toString();
        String expected = "XY";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatMultiplyWorksWithYAnd1() {
        IntegerTerm it1 = new IntegerTerm(1);
        VariableTerm vt2 = new VariableTerm("Y");

        Product product3 = new Product();
        product3.addExpression(it1);
        product3.addExpression(vt2);

        Expression e = product3.findElementAt(0);
        Expression newEx = product3.merge(e, false);
        String actual = newEx.toString();
        String expected = "Y";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatMultiplyWorksWithYAndNeg1() {
        IntegerTerm it1 = new IntegerTerm(-1);
        VariableTerm vt2 = new VariableTerm("Y");

        Product product3 = new Product();
        product3.addExpression(it1);
        product3.addExpression(vt2);

        Expression e = product3.findElementAt(0);
        Expression newEx = product3.merge(e, false);
        String actual = newEx.toString();
        String expected = "-Y";
        assertEquals(expected, actual);
    }

    @Test
    public void testThatMultiplyWorksWithNegYAnd1() {
        IntegerTerm it1 = new IntegerTerm(1);
        VariableTerm vt2 = new VariableTerm("-Y");

        Product product3 = new Product();
        product3.addExpression(it1);
        product3.addExpression(vt2);

        Expression e = product3.findElementAt(0);
        Expression newEx = product3.merge(e, false);
        String actual = newEx.toString();
        String expected = "-Y";
        assertEquals(expected, actual);
    }
}
