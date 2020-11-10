package com.l3rnz.algebrain.parser;

import com.l3rnz.algebrain.domain.Expression;
import com.l3rnz.algebrain.domain.Token;
import com.l3rnz.algebrain.exception.ExpressionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Parser2Test {

    @Test
    public void testThatTokenizeGeneratesAList() {
        Parser2 parser = new Parser2();
        List<Token> list = parser.tokenize("3");
        assertNotNull(list);
    }

    @Test
    public void testThatTokenContainsString() {
        Parser2 parser = new Parser2();
        String expected = "3";
        List<Token> list = parser.tokenize("3");

        Token token = list.get(0);
        String actual = token.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testThatTokenParsesIntoTwoParts() {
        Parser2 parser = new Parser2();
        List<Token> list = parser.tokenize("3A");
        int expected = 2;

        int actual= list.size();

        assertEquals(expected, actual);
    }

    @Test
    public void testThatTokenWithMultipleCharactersParsesIntoOnePart() {
        Parser2 parser = new Parser2();
        List<Token> list = parser.tokenize("35");
        int expected = 1;

        int actual= list.size();

        assertEquals(expected, actual);
    }

    @Test
    public void testThatTokenWithMultipleCharactersParsesIntoTwoParts() {
        Parser2 parser = new Parser2();
        List<Token> list = parser.tokenize("35Abc");
        int expected = 2;

        int actual= list.size();

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"6Boys>=12","6Boys<=12","6Boys!=12"})
    public void testThatInequalitiesParse(String input) {
        Parser2 parser = new Parser2();
        List<Token> list = parser.tokenize(input);
        int expected = 4;

        int actual= list.size();

        assertEquals(expected, actual);
    }


    @Test
    public void testThatFullEquationTokenizesIntoPartsCorrectly() {
        Parser2 parser = new Parser2();
        List<Token> list = parser.tokenize("3.45-17A*6(B+-12)=3^[Bob+1]");
        int expected = 20;

        int actual= list.size();

        assertEquals(expected, actual);
    }

    @Test
    public void testThatTwoDecimalPointsDontWork() {
        Parser2 parser = new Parser2();
        assertThrows(ExpressionException.class, () -> {
            parser.tokenize("3.4.5");
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "35", "3.8", "A", "Frederick", "-3", "--4.5", "-Bobby", "----1234", "2*3",
            "2*3*4", "-3.5*-Bob", "3+4", "3.5-Bob", "3--Bob", "-3--4.5-Bob", "-3+-4.5-Bob",
            "3A"
    })
    public void testThatParseWorksWithExpression(String expected) {
        Parser2 parser = new Parser2();
        Expression expression = parser.parse(expected);
        String actual = expression.toString();
        assertEquals(expected, actual);
    }

}
