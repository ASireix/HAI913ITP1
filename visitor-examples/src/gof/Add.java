package gof;

/**
 * Élément concret composite : une addition.
 */
public record Add(Expr left, Expr right) implements Expr {

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitAddition(this);
    }
}
