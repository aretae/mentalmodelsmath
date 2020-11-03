package com.l3rnz.algebrain.domain;

/**
 * Superclass for ProductPart and SumPart.
 */
public abstract class IndefiniteSizeExpressionPart {
    protected final Expression term;

    public IndefiniteSizeExpressionPart(final Expression t) {
        term = t;
    }



    public IndefiniteSizeExpressionPart(final IndefiniteSizeExpressionPart part) {
        this.term = part.getContainedExpression();
    }

    @Override
    public String toString() {
        return term.toString();
    }

    public abstract boolean displayOperator();

    public Expression getContainedExpression() {
        return term;
    }

    public boolean contains(final Expression e) {
        return e.equals(term);
    }

    public abstract IndefiniteSizeExpressionPart duplicate();
}
