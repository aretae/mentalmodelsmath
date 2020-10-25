package com.l3rnz.algebrain.domain;

/**
 * Term is a single-valued part of an equation that arithmetic can work on.
 *
 * @param <T>  What kind of term: Integer, Double
 */
public abstract class Term<T> implements Expression {

    protected int negativeCount;
    protected T data;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < negativeCount; i++) {
            builder.append("-");
        }
        builder.append(data);
        return builder.toString();
    }

    public boolean isNegative() {
        return negativeCount > 0;
    }

    public abstract Term addValue(Term ex);

    public int getNegativeMultiplier() {
        return negativeCount % 2 != 0 ? -1 : 1;
    }

    public abstract T getValue();

    public void addNegative() {
        negativeCount++;
    }

    protected void setData(T data) {
        this.data = data;
    }

    public int getNegativeCount() {
        return negativeCount;
    }
}
