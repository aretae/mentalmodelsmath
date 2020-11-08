package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;

/**
 * Represents an arithmetic sum.
 * Contains SumParts
 */
public class Sum extends IndefiniteSizeExpression {

    @Override
    public String getOperatorString() {
        return "+";
    }

    @Override
    public IndefiniteSizeExpressionPart build(final IndefiniteSizeExpressionPart part) {
        if (part instanceof SumPart) {
            return new SumPart((SumPart) part);
        }
        throw new ExpressionException();
    }

    @Override
    public IndefiniteSizeExpressionPart convertToExpressionPart(final Term iTerm) {
        return new SumPart(iTerm);
    }

    @Override
    public boolean checkForDecomposableType(final Expression expression) {
        return !(expression instanceof Sum);
    }

    @Override
    public Term compute(final Expression e, final Expression ex) {
        if (e instanceof Term) {
            final Term t = (Term) e;
            return t.addValue((Term) ex);
        }
        return null;
    }
}
