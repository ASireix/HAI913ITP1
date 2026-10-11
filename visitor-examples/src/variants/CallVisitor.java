package variants;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

/**
 * Variante 4 : un second visiteur spécialisé, appliqué dans une seconde
 * passe. Simplifié : noms seulement, sans bindings ni type du receveur
 * (que l'étape A3 doit, elle, extraire).
 */
public class CallVisitor extends ASTVisitor {

    private final ToyFacts.Builder facts;
    private String currentMethod = "?";

    public CallVisitor(ToyFacts.Builder facts) {
        this.facts = facts;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        currentMethod = node.getName().getIdentifier();
        return true;
    }

    @Override
    public boolean visit(MethodInvocation node) {
        facts.addCall(currentMethod, node.getName().getIdentifier());
        return true;
    }
}
