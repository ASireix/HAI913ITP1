package jdt;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;

/**
 * ASTVisitor de JDT : ordre de visite, valeur de retour de visit(...) et
 * élagage d'un sous-arbre. L'exemple compte les if et les littéraux de chaîne
 * par méthode, sans bindings.
 */
public final class JdtVisitorDemo {

    static final String SOURCE = String.join("\n",
            "class Shop {",
            "    String label(int stock) {",
            "        if (stock == 0) { return \"empty\"; }",
            "        return \"ok\";",
            "    }",
            "    Runnable task() {",
            "        return new Runnable() {",
            "            public void run() {",
            "                if (true) { System.out.println(\"inside\"); }",
            "            }",
            "        };",
            "    }",
            "}");

    private JdtVisitorDemo() {
    }

    public static void main(String[] args) {
        ASTParser parser = ASTParser.newParser(AST.getJLSLatest());
        Map<String, String> options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(JavaCore.VERSION_17, options);
        parser.setCompilerOptions(options);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setSource(SOURCE.toCharArray());
        CompilationUnit unit = (CompilationUnit) parser.createAST(null);

        System.out.println("1) Ordre de visite des premiers noeuds :");
        unit.accept(new ASTVisitor() {
            int depth;
            int shown;

            @Override
            public boolean preVisit2(ASTNode node) {
                if (shown++ < 8) {
                    System.out.println("   " + "  ".repeat(depth) + "entre "
                            + node.getClass().getSimpleName());
                }
                depth++;
                return true;
            }

            @Override
            public void postVisit(ASTNode node) {
                depth--;
            }
        });

        System.out.println("2) Par methode, classes anonymes elaguees :");
        unit.accept(new ASTVisitor() {
            String method = "?";
            int ifs;
            int strings;

            @Override
            public boolean visit(MethodDeclaration node) {
                method = node.getName().getIdentifier();
                ifs = 0;
                strings = 0;
                return true;
            }

            @Override
            public void endVisit(MethodDeclaration node) {
                if (node.getName().getIdentifier().equals(method)) {
                    int first = unit.getLineNumber(node.getStartPosition());
                    int last = unit.getLineNumber(
                            node.getStartPosition() + node.getLength() - 1);
                    System.out.println("   " + method + " : if=" + ifs
                            + ", chaines=" + strings
                            + " (lignes " + first + "-" + last + ")");
                }
            }

            @Override
            public boolean visit(IfStatement node) {
                ifs++;
                return true;
            }

            @Override
            public boolean visit(StringLiteral node) {
                strings++;
                return true;
            }

            @Override
            public boolean visit(AnonymousClassDeclaration node) {
                return false; // le corps de la classe anonyme est ignoré
            }
        });
    }
}
