package gof;

/**
 * Élément concret composite : une multiplication.
 */
public record Mul(Expr left, Expr right) implements Expr {

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitMultiplication(this);
    }
}
