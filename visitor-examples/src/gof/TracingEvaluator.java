package gof;

import java.util.function.Supplier;

/**
 * Evaluator instrumenté : affiche chaque visite et sa valeur, dans l'ordre
 * réel d'exécution. Il ne sert qu'à observer le double aiguillage.
 */
public class TracingEvaluator extends Evaluator {

    private int depth;

    @Override
    public Integer visitNumber(Num number) {
        return trace("visitNumber(" + number.value() + ")",
                () -> super.visitNumber(number));
    }

    @Override
    public Integer visitAddition(Add addition) {
        return trace("visitAddition", () -> super.visitAddition(addition));
    }

    @Override
    public Integer visitMultiplication(Mul multiplication) {
        return trace("visitMultiplication",
                () -> super.visitMultiplication(multiplication));
    }

    private Integer trace(String call, Supplier<Integer> body) {
        System.out.println("  ".repeat(depth + 1) + call);
        depth++;
        Integer result = body.get();
        depth--;
        System.out.println("  ".repeat(depth + 1) + "<- " + result);
        return result;
    }
}
