package com.l3rnz.algebrain.domain;

/**
 * Product Part is the holder class for Expressions that go in a Product.
 */
public class ProductPart extends IndefiniteSizeExpressionPart {
    public ProductPart(final Expression t) {
        super(t);
    }

    public ProductPart(final ProductPart part) {
        super(part);
    }

    @Override
    public boolean displayOperator() {
        return true;
    }


    @Override
    public ProductPart duplicate() {
        return new ProductPart(term);
    }
}
