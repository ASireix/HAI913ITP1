package spoondemo;

import java.util.List;

import spoon.Launcher;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtCatch;
import spoon.reflect.code.CtIf;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.CtScanner;
import spoon.reflect.visitor.filter.TypeFilter;

/**
 * Trois façons de parcourir un modèle Spoon : scanner (visiteur),
 * filtre (requête) et processeur.
 */
public final class SpoonDemo {

    static final String SOURCE = String.join("\n",
            "class Shop {",
            "    String label(int stock) {",
            "        if (stock == 0) { return \"empty\"; }",
            "        try { Integer.parseInt(\"x\"); }",
            "        catch (NumberFormatException e) { }",
            "        return \"ok\";",
            "    }",
            "    int twice(int n) { return 2 * n; }",
            "}");

    private SpoonDemo() {
    }

    public static void main(String[] args) {
        CtClass<?> shop = Launcher.parseClass(SOURCE);

        // 1) Scanner : le visiteur de Spoon. On redéfinit visitCtXxx et on
        //    rappelle super pour descendre (le « return true » de JDT).
        int[] ifs = new int[1];
        shop.accept(new CtScanner() {
            @Override
            public void visitCtIf(CtIf ifElement) {
                ifs[0]++;
                super.visitCtIf(ifElement);
            }
        });
        System.out.println("1) scanner    : " + ifs[0] + " if");

        // 2) Filtre : requête déclarative sur un sous-arbre.
        List<CtMethod<?>> methods =
                shop.getElements(new TypeFilter<>(CtMethod.class));
        for (CtMethod<?> m : methods) {
            System.out.println("2) filtre     : methode " + m.getSimpleName()
                    + " lignes " + m.getPosition().getLine()
                    + "-" + m.getPosition().getEndLine());
        }

        // 3) Processeur : traitement d'un type d'élément. Déclenché ici à
        //    la main ; launcher.addProcessor(...) le ferait appliquer par
        //    Spoon à tout le modèle.
        AbstractProcessor<CtCatch> emptyCatch = new AbstractProcessor<>() {
            @Override
            public boolean isToBeProcessed(CtCatch candidate) {
                return candidate.getBody().getStatements().isEmpty();
            }

            @Override
            public void process(CtCatch element) {
                System.out.println("3) processeur : catch vide ligne "
                        + element.getPosition().getLine());
            }
        };
        shop.getElements(new TypeFilter<>(CtCatch.class)).stream()
                .filter(emptyCatch::isToBeProcessed)
                .forEach(emptyCatch::process);
    }
}
