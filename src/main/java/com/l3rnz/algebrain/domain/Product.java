package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;

/**
 * Represents a product.
 */
public class Product extends IndefiniteSizeExpression {
    public Product() {
        super();
    }

    public String getOperatorString() {
        return "*";
    }

    @Override
    public IndefiniteSizeExpressionPart build(IndefiniteSizeExpressionPart part) {
        if (part instanceof ProductPart) {
            return new ProductPart((ProductPart)part);
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
    public IndefiniteSizeExpressionPart convertToExpressionPart(Term iTerm) {
        return new ProductPart(iTerm);
    }

    @Override
    public boolean checkForDecomposableType(Expression expression) {
        return !(expression instanceof Product);
    }

    @Override
    public Term compute(Expression e, Expression ex) {
        if (e instanceof Term) {
            Term t = (Term) e;
            return t.multiplyValue((Term) ex);
        }
        return null;
    }
}
