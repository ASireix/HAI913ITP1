package gof;

/**
 * Visiteur concret n° 1 : calcule la valeur.
 * Le parcours des enfants est à la charge du visiteur.
 */
public class Evaluator implements ExprVisitor<Integer> {

    @Override
    public Integer visitNumber(Num number) {
        return number.value();
    }

    @Override
    public Integer visitAddition(Add addition) {
        return addition.left().accept(this) + addition.right().accept(this);
    }

    @Override
    public Integer visitMultiplication(Mul multiplication) {
        return multiplication.left().accept(this)
                * multiplication.right().accept(this);
    }
}
