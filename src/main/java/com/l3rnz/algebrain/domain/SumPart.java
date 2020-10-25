package com.l3rnz.algebrain.domain;


/**
 * One element of a Sum.
 */
public class SumPart {

    private final Term term;
    private final boolean explicit;

    public SumPart(final Term t) {
        term = t;
        explicit = false;
    }

    public SumPart(final Term t, final boolean e) {
        term = t;
        explicit = e;
    }

    public SumPart(final SumPart part) {
        this.explicit = part.explicit;
        this.term = part.term;
    }

    @Override
    public String toString() {
        return term.toString();
    }

    public boolean displayPlus() {
        return explicit || (!term.isNegative());
    }


    public Term getContainedExpression() {
        return term;
    }

    public boolean contains(final Term e) {
        return e.equals(term);
    }

    public SumPart duplicate() {
        return new SumPart(term, explicit);
    }

}
