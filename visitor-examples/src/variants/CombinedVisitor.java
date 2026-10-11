package variants;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Variante 5 : un seul parcours pour la structure et les appels. Possible,
 * mais les deux préoccupations partagent l'état, et le corps des méthodes ne
 * peut plus être élagué pour la structure.
 */
public class CombinedVisitor extends ASTVisitor {

    private final ToyFacts.Builder facts;
    private String currentType = "?";
    private String currentMethod = "?";

    public CombinedVisitor(ToyFacts.Builder facts) {
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
        currentMethod = node.getName().getIdentifier();
        facts.addMethod(currentType, currentMethod);
        return true; // il faut descendre : les appels sont dans le corps
    }

    @Override
    public boolean visit(MethodInvocation node) {
        facts.addCall(currentMethod, node.getName().getIdentifier());
        return true;
    }
}
