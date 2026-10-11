package gof;

/**
 * Visiteur concret n° 2 : une nouvelle opération (l'affichage), ajoutée
 * sans modifier Num, Add ni Mul.
 */
public class Printer implements ExprVisitor<String> {

    @Override
    public String visitNumber(Num number) {
        return Integer.toString(number.value());
    }

    @Override
    public String visitAddition(Add addition) {
        return "(" + addition.left().accept(this)
                + " + " + addition.right().accept(this) + ")";
    }

    @Override
    public String visitMultiplication(Mul multiplication) {
        return multiplication.left().accept(this)
                + " * " + multiplication.right().accept(this);
    }
}
