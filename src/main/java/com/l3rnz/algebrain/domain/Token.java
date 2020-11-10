package com.l3rnz.algebrain.domain;

import java.util.Objects;

public class Token {
    String content;

    public Token(String s) {
        content = s;
    }

    public String toString() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(content, token.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
