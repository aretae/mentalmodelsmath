package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;

import java.util.ArrayList;
import java.util.List;

/**
 * Superclass for Product and Sum.
 */
public abstract class IndefiniteSizeExpression implements Expression {
    protected final List<IndefiniteSizeExpressionPart> terms;

    public IndefiniteSizeExpression() {
        terms = new ArrayList<>();
    }

    public abstract boolean checkForDecomposableType(Expression expression);

    public abstract IndefiniteSizeExpressionPart convertToExpressionPart(Term iTerm);

    public void addExpression(final Expression expression) {
        if (checkForDecomposableType(expression)) {
            final Term iTerm = (Term) expression;
            addExpression(iTerm);
        } else {
            for (IndefiniteSizeExpressionPart part : ((IndefiniteSizeExpression) expression).terms) {
                terms.add(part.duplicate());
            }
        }
    }

    private void addExpression(final Term iTerm) {
        terms.add(convertToExpressionPart(iTerm));
    }

    public void addExpression(final Expression term, final boolean explicit) {
        terms.add(new SumPart(term, explicit));
    }

    @Override
    public String toString() {
        final StringBuilder output = new StringBuilder();
        for (int i = 0; i < terms.size(); i++) {
            if (terms.get(i).displayOperator() && i > 0) {
                output.append(getOperatorString());
            }
            output.append(terms.get(i));
        }
        return output.toString();
    }

    public abstract String getOperatorString();

    public Expression findElementAt(final int i) {
        if (i < 0) {
            throw new ExpressionException();
        }
        int currentSize = 0;
        for (IndefiniteSizeExpressionPart part : terms) {
            currentSize += part.toString().length();
            if (i < currentSize) {
                return part.getContainedExpression();
            }
        }
        throw new ExpressionException();
    }

    public int findIndexOf(final Expression e) {
        for (int i = 0; i < terms.size(); i++) {
            if (terms.get(i).contains(e)) {
                return i;
            }
        }
        return -1;
    }

    public boolean contains(final Expression e) {
        return findIndexOf(e) >= 0;
    }

    public abstract IndefiniteSizeExpressionPart build(IndefiniteSizeExpressionPart part);

    public void move(final Expression e, final int i) throws ExpressionException {
        if (!contains(e)) {
            throw new ExpressionException();
        }
        IndefiniteSizeExpressionPart thePart = null;
        for (IndefiniteSizeExpressionPart part : terms) {
            if (part.contains(e)) {
                thePart = part;
            }
        }
        terms.add(i, build(thePart));
        terms.remove(thePart);
    }

    public Expression merge(final Expression e, final boolean left) {
        final int i = findIndexOf(e);
        checkForIllegalMerge(left, i);
        final Expression ex = terms.get(i + (left ? -1 : 1)).getContainedExpression();
        return compute(e, ex);
    }

    public abstract Expression compute(Expression e, Expression ex);


    private void checkForIllegalMerge(final boolean left, final int i) {
        if (i == -1) {
            throw new ExpressionException();
        }
        if (i == 0 && left) {
            throw new ExpressionException();
        }
        if (i == terms.size() - 1 && !left) {
            throw new ExpressionException();
        }
    }
}
