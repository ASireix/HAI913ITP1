package variants;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * Variante 6 : l'orchestrateur de la chaîne. Il reçoit les AST produits par
 * l'extracteur de modèle, applique les visiteurs spécialisés à chaque unité
 * de compilation et rend un modèle de faits indépendant de JDT.
 */
public final class FactExtractor {

    private FactExtractor() {
    }

    public static ToyFacts extract(List<CompilationUnit> units) {
        ToyFacts.Builder facts = new ToyFacts.Builder();
        for (CompilationUnit unit : units) {
            unit.accept(new StructureVisitor(facts)); // passe 1
            unit.accept(new CallVisitor(facts));      // passe 2
        }
        return facts.build();
    }
}
