package gof;

/**
 * Élément concret : une constante.
 */
public record Num(int value) implements Expr {

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitNumber(this); // l'élément choisit sa méthode visit
    }
}
