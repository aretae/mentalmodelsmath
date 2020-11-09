package com.l3rnz.algebrain.parser;

import com.l3rnz.algebrain.domain.Token;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    public void testThatTokenWithMultipleCharactersParsesIntoTwoParts() {
        Parser2 parser = new Parser2();
        List<Token> list = parser.tokenize("3Bob");
        int expected = 2;

        int actual= list.size();

        assertEquals(expected, actual);
    }

}
