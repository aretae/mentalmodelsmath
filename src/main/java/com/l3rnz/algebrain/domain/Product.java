package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;

/**
 * Represents a product.
 */
public class Product extends IndefiniteSizeExpression {

    @Override
    public String getOperatorString() {
        return "*";
    }

    @Override
    public IndefiniteSizeExpressionPart build(final IndefiniteSizeExpressionPart part) {
        if (part instanceof ProductPart) {
            return new ProductPart((ProductPart) part);
        }
        throw new ExpressionException();
    }

    @Override
    public void addExpression(final Expression expression) {
        if (expression instanceof Sum) {
            throw new ExpressionException();
        }
        super.addExpression(expression);
    }

    @Override
    public IndefiniteSizeExpressionPart convertToExpressionPart(final Term iTerm) {
        return new ProductPart(iTerm);
    }

    @Override
    public boolean checkForDecomposableType(final Expression expression) {
        return !(expression instanceof Product);
    }

    @Override
    public Term compute(final Expression e, final Expression ex) {
        if (e instanceof Term) {
            final Term t = (Term) e;
            return t.multiplyValue((Term) ex);
        }
        return null;
    }
}
