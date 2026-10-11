package gof;

/**
 * Visiteur (rôle « Visitor ») : une méthode visit par type concret d'élément.
 *
 * @param <R> type du résultat produit par la visite
 */
public interface ExprVisitor<R> {

    R visitNumber(Num number);

    R visitAddition(Add addition);

    R visitMultiplication(Mul multiplication);
}
