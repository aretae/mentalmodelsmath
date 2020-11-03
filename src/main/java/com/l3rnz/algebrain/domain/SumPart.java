package com.l3rnz.algebrain.domain;


/**
 * One element of a Sum.
 */
public class SumPart extends IndefiniteSizeExpressionPart {

    private final boolean explicit;

    public SumPart(final Expression t) {
        this(t,false);
    }

    public SumPart(final Expression t, final boolean e) {
        super(t);
        explicit = e;
    }

    public SumPart(final SumPart part) {
        super(part);
        this.explicit = part.explicit;
    }


    @Override
    public boolean displayOperator() {
        return explicit || (term instanceof Term && !((Term)term).isNegative());
    }


    @Override
    public SumPart duplicate() {
        return new SumPart(term, explicit);
    }
}
