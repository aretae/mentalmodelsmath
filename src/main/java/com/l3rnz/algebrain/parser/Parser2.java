package com.l3rnz.algebrain.parser;

import com.l3rnz.algebrain.domain.Token;

import java.util.ArrayList;
import java.util.List;

public class Parser2 {


    private ArrayList<Token> tokens;

    public Parser2() {
        tokens = new ArrayList<Token>();
    }

    public List<Token> tokenize(String s) {
        tokens = new ArrayList<Token>();
        char[] chars = s.toCharArray();
        int currentIndex = 0;
        String currentString;
        for (; currentIndex < chars.length; currentIndex++) {
            currentString = chars[currentIndex] + "";
            tokens.add(new Token(currentString));
        }
        return tokens;
    }
}
