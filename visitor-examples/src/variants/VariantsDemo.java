package variants;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodInvocation;

/**
 * Les six façons d'utiliser un visiteur JDT de l'annexe 3, appliquées au
 * même petit programme de deux fichiers.
 */
public final class VariantsDemo {

    static final String LIBRARY = String.join("\n",
            "class Library {",
            "    java.util.List<String> titles = new java.util.ArrayList<>();",
            "    void add(String t) { titles.add(t); log(t); }",
            "    void log(String t) { System.out.println(t); }",
            "}");
    static final String APP = String.join("\n",
            "class App {",
            "    static void main(String[] a) { new Library().add(\"Dune\"); }",
            "}");

    private VariantsDemo() {
    }

    public static void main(String[] args) {
        List<CompilationUnit> units = List.of(parse(LIBRARY), parse(APP));

        // 1) Visiteur anonyme, ad hoc : une observation locale et jetable.
        int[] calls = new int[1];
        for (CompilationUnit unit : units) {
            unit.accept(new ASTVisitor() {
                @Override
                public boolean visit(MethodInvocation node) {
                    calls[0]++;
                    return true;
                }
            });
        }
        System.out.println("1) anonyme     : " + calls[0] + " appels");

        // 2) Classe nommée qui garde son état.
        MethodNameCollector collector = new MethodNameCollector();
        units.forEach(unit -> unit.accept(collector));
        System.out.println("2) nommee      : " + collector.names());

        // 3 + 4 + 6) Visiteurs spécialisés -> accumulateur, via l'orchestrateur.
        ToyFacts facts = FactExtractor.extract(units);
        System.out.println("3-4-6) chaine  : types   " + facts.types());
        System.out.println("                 methods " + facts.methods());
        System.out.println("                 calls   " + facts.calls());

        // 5) Un seul visiteur combiné : même résultat, état partagé.
        ToyFacts.Builder builder = new ToyFacts.Builder();
        units.forEach(unit -> unit.accept(new CombinedVisitor(builder)));
        System.out.println("5) combine     : meme resultat = "
                + facts.equals(builder.build()));

        // Le traitement ne voit que les faits.
        System.out.println("   traitement  : "
                + ToyMetrics.methodsPerType(facts));
    }

    static CompilationUnit parse(String source) {
        ASTParser parser = ASTParser.newParser(AST.getJLSLatest());
        parser.setSource(source.toCharArray());
        return (CompilationUnit) parser.createAST(null);
    }
}
