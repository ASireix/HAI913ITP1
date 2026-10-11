package gof;

/**
 * Élément visitable (rôle « Element » du patron Visitor) :
 * un nœud d'un petit arbre d'expressions.
 */
public interface Expr {

    <R> R accept(ExprVisitor<R> visitor);
}
