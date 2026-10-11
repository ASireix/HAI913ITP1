package variants;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * Variante 2 : une classe nommée, dédiée à une préoccupation, qui garde son
 * propre état et l'expose après la visite.
 */
public class MethodNameCollector extends ASTVisitor {

    private final List<String> names = new ArrayList<>();

    @Override
    public boolean visit(MethodDeclaration node) {
        names.add(node.getName().getIdentifier());
        return true;
    }

    public List<String> names() {
        return names;
    }
}
