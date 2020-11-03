package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;

/**
 * Represents an arithmetic sum.
 * Contains SumParts
 */
public class Sum extends IndefiniteSizeExpression {

    public Sum() {
        super();
    }


    public String getOperatorString() {
        return "+";
    }

    @Override
    public IndefiniteSizeExpressionPart build(IndefiniteSizeExpressionPart part) {
        if (part instanceof SumPart) {
            return new SumPart((SumPart)part);
        }
        throw new ExpressionException();
    }

    @Override
    public IndefiniteSizeExpressionPart convertToExpressionPart(Term iTerm) {
        return new SumPart(iTerm);
    }

    @Override
    public boolean checkForDecomposableType(Expression expression) {
        return !(expression instanceof Sum);
    }

    @Override
    public Term compute(Expression e, Expression ex) {
        if (e instanceof Term) {
            Term t = (Term) e;
            return t.addValue((Term) ex);
        }
        return null;
    }
}
