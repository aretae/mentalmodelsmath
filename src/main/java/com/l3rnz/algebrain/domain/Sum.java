package com.l3rnz.algebrain.domain;

import com.l3rnz.algebrain.exception.ExpressionException;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an arithmetic sum.
 * Contains SumParts
 */
public class Sum implements Expression {

    private final List<SumPart> terms;

    public Sum() {
        terms = new ArrayList<>();
    }

    public void addTerm(final Expression term) {
        if (!(term instanceof Sum)) {
            final Term iTerm = (Term) term;
            addTerm(iTerm);
        } else {
            for (SumPart part: ((Sum) term).terms) {
                terms.add(part.duplicate());
            }
        }
    }

    private void addTerm(final Term iTerm) {
        terms.add(new SumPart(iTerm));
    }

    public void addTerm(final Term term, final boolean explicit) {
        terms.add(new SumPart(term, explicit));
    }

    @Override
    public String toString() {
        final StringBuilder output = new StringBuilder();
        for (int i = 0; i < terms.size(); i++) {
            if (terms.get(i).displayPlus() && i > 0) {
                output.append("+");
            }
            output.append(terms.get(i));
        }
        return output.toString();
    }

    public Term findElementAt(final int i) {
        if (i < 0) {
            throw new ExpressionException();
        }
        int currentSize = 0;
        for (SumPart part: terms) {
            currentSize += part.toString().length();
            if (i < currentSize) {
                return part.getContainedExpression();
            }
        }
        throw new ExpressionException();
    }

    public int findIndexOf(final Term e) {
        for (int i = 0; i < terms.size(); i++) {
            if (terms.get(i).contains(e)) {
                return i;
            }
        }
        return -1;
    }

    public boolean contains(final Term e) {
        return findIndexOf(e) >= 0;
    }


    public void move(final Term e, final int i) throws ExpressionException {
        if (!contains(e)) {
            throw new ExpressionException();
        }
        SumPart thePart = null;
        for (SumPart part: terms) {
            if (part.contains(e)) {
                thePart = part;
            }
        }
        terms.add(i, new SumPart(thePart));
        terms.remove(thePart);
    }


    public Expression merge(final Term e, final boolean left) {
        final int i = findIndexOf(e);
        checkForIllegalMerge(left, i);
        final Term ex = terms.get(i + (left ? -1 : 1)).getContainedExpression();
        return e.addValue(ex);
    }

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
