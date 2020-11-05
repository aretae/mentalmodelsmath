package com.l3rnz.algebrain.parser;

import com.l3rnz.algebrain.domain.Expression;
import com.l3rnz.algebrain.exception.ExpressionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class ExpressionParserTest {

    @ParameterizedTest
    @ValueSource(strings =
        {
          "3", "-23", "2+3", "2+3+4", "2+-3", "2+-3+-4", "2.3", "-2.3+-5+3.7+-2.8", "4-3", "--3"
          , "X", "-X", "3-X", "4.3+2--Bob", "3*2", "-3*X", "--4*--2.2*-Bob", "3+4*5", "-32*3.1415-35*152*--X+Bob"
          , "3X", "3XY", "-2.2Bob", "---3.5IsHalfOfSeven", "XYBob"
        })
    public void testThatParserParsesIntoEquationThatRendersSame(String input) {
        ExpressionParser parser = new ExpressionParser();
        Expression expression = parser.parse(input);
        String actual = expression.toString();
        assertEquals(input, actual, input+":"+actual);
    }

    @Test
    public void testThatParserFailsWithBadData() {
        ExpressionParser parser = new ExpressionParser();
        assertThrows(ExpressionException.class, () -> {
            parser.parse("z1b1b");
        });
    }

    @Test
    public void testThatParserFailsWithBadData2() {
        ExpressionParser parser = new ExpressionParser();
        assertThrows(ExpressionException.class, () -> {
            parser.parse("z");
        });
    }

}
