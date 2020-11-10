package com.l3rnz.algebrain.domain;

public class ExpressionConstants {

    public static final String INTEGER_TERM_REGEX = "^[-]*[0-9]+$";
    public static final String IMPLICIT_PRODUCT_TERM_REGEX = "^[-]*(([0-9]+([.][0-9]+)?)|([A-Z][a-z]*))([A-Z][a-z]*)+$";
    public static final String EMPTY_STRING = "";
    public static final String DECIMAL_TERM_REGEX = "^[-]*[0-9]+\\.[0-9]+$";
    public static final String DIGITS_STRING = "01234567890";
    public static final char DOT_CHAR = '.';
    public static final String DOT_STRING = ".";
    public static final char EQUALS_CHAR = '=';
    public static final String INEQUALITY_PARTS_STRING = "><!";
    public static final String ALPHABET_CAPITALS_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String OPERATORS_STRING = "+-*/^()[]=><!";
    public static final String ALPHABET_LOWER_CASE_STRING = "abcdefghijklmnopqrstuvwxyz";
    public static final String PARENTHETICAL_TERM_REGEX = "^\\(.*\\)$";
    public static final String VARIABLE_NAME_REGEX = "([A-Z][a-z]*)";
    public static final String DECIMAL_WITH_NEGATIVES_REGEX = "([-]*[0-9]+([.][0-9]+)?)";
    public static final char MINUS_CHARACTER = '-';
    public static final char SPACE_CHARACTER = ' ';
    public static final String MINUS_STRING = "-";
    public static final String VARIABLE_TERM_WITH_NEGATIVE_REGEX = "^[-]*[A-Z][a-z]*$";
}
