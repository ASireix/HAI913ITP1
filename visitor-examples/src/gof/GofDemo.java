package gof;

/**
 * Client : construit la structure 2 * (3 + 4), puis lui applique deux
 * opérations différentes, chacune portée par un visiteur.
 */
public final class GofDemo {

    private GofDemo() {
    }

    public static void main(String[] args) {
        // La structure d'objets (ObjectStructure) : un arbre d'expressions.
        Expr expr = new Mul(new Num(2), new Add(new Num(3), new Num(4)));

        // Même structure, deux opérations : deux visiteurs concrets.
        System.out.println("Printer   : " + expr.accept(new Printer()));
        System.out.println("Evaluator : " + expr.accept(new Evaluator()));

        // Ordre réel des visites (diagramme de séquence de l'annexe 3).
        System.out.println("Trace de l'evaluation :");
        expr.accept(new TracingEvaluator());
    }
}
