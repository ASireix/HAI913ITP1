package variants;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Variante 3 : le visiteur observe les nœuds JDT et écrit dans un
 * accumulateur externe. Volontairement simplifié : ignore énumérations,
 * records et types imbriqués (que l'étape A2 doit, elle, traiter).
 */
public class StructureVisitor extends ASTVisitor {

    private final ToyFacts.Builder facts;
    private String currentType = "?";

    public StructureVisitor(ToyFacts.Builder facts) {
        this.facts = facts;
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        currentType = node.getName().getIdentifier();
        facts.addType(currentType);
        return true;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        facts.addMethod(currentType, node.getName().getIdentifier());
        return false; // le corps ne concerne pas la structure : élagué
    }
}
