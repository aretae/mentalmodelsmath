package com.l3rnz.algebrain.domain;

/**
 * Term is a single-valued part of an equation that arithmetic can work on.
 *
 * @param <T>  What kind of term: Integer, Double, String
 */
public abstract class Term<T> implements Expression {

    protected int negativeCount;
    private T data;

    public Term() {
        //Empty for subclasses
    }

    public Term(final String data) {
        checkDataValidity(data);
        setNegatives(data);
        setData(convertToType(stripNegatives(data)));
    }

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

    public abstract Expression addValue(Expression ex);

    public int getNegativeMultiplier() {
        return negativeCount % 2 != 0 ? -1 : 1;
    }

    public void addNegative() {
        negativeCount++;
    }

    protected void setData(final T data) {
        this.data = data;
    }

    public int getNegativeCount() {
        return negativeCount;
    }

    public T getData() {
        return data;
    }

    public abstract Term multiplyValue(Term ex);

    protected void resetNegatives() {
        this.negativeCount = 0;
    }

    public abstract T convertToType(String data);

    public abstract void checkDataValidity(String data);

    public String stripNegatives(final String data) {
        return data.replace(ExpressionConstants.MINUS_CHARACTER, ExpressionConstants.SPACE_CHARACTER).trim();
    }

    public void setNegatives(final String data) {
        for (char datum: data.toCharArray()) {
            if (datum == ExpressionConstants.MINUS_CHARACTER) {
                addNegative();
            } else {
                break;
            }
        }
    }
}
