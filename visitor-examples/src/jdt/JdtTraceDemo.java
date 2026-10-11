package jdt;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Trace des appels visit / endVisit sur le programme de JdtVisitorDemo :
 * c'est l'infrastructure de JDT qui parcourt l'arbre ; le visiteur décide
 * seulement, par sa valeur de retour, si les enfants sont visités.
 */
public final class JdtTraceDemo {

    private JdtTraceDemo() {
    }

    public static void main(String[] args) {
        ASTParser parser = ASTParser.newParser(AST.getJLSLatest());
        Map<String, String> options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(JavaCore.VERSION_17, options);
        parser.setCompilerOptions(options);
        parser.setSource(JdtVisitorDemo.SOURCE.toCharArray());
        CompilationUnit unit = (CompilationUnit) parser.createAST(null);

        unit.accept(new ASTVisitor() {
            int depth;

            private boolean enter(String node, boolean children) {
                System.out.println("  ".repeat(depth) + "visit(" + node
                        + ") -> " + children);
                depth++;
                return children;
            }

            private void leave(String node) {
                depth--;
                System.out.println("  ".repeat(depth) + "endVisit(" + node
                        + ")");
            }

            @Override
            public boolean visit(TypeDeclaration node) {
                return enter("TypeDeclaration " + node.getName(), true);
            }

            @Override
            public void endVisit(TypeDeclaration node) {
                leave("TypeDeclaration " + node.getName());
            }

            @Override
            public boolean visit(MethodDeclaration node) {
                return enter("MethodDeclaration " + node.getName(), true);
            }

            @Override
            public void endVisit(MethodDeclaration node) {
                leave("MethodDeclaration " + node.getName());
            }

            @Override
            public boolean visit(IfStatement node) {
                return enter("IfStatement", true);
            }

            @Override
            public void endVisit(IfStatement node) {
                leave("IfStatement");
            }

            @Override
            public boolean visit(AnonymousClassDeclaration node) {
                return enter("AnonymousClassDeclaration", false); // élagage
            }

            @Override
            public void endVisit(AnonymousClassDeclaration node) {
                leave("AnonymousClassDeclaration");
            }
        });
    }
}
