package variants;

import java.util.ArrayList;
import java.util.List;

/**
 * Modèle de faits « jouet » : ce que les visiteurs remplissent et ce que les
 * traitements lisent. Aucune classe de JDT n'y apparaît. (Le TP demande un
 * modèle plus riche : voir l'annexe 1.)
 */
public record ToyFacts(List<String> types, List<String> methods,
        List<String> calls) {

    /** Accumulateur externe, rempli pendant les visites. */
    public static final class Builder {
        private final List<String> types = new ArrayList<>();
        private final List<String> methods = new ArrayList<>();
        private final List<String> calls = new ArrayList<>();

        public void addType(String type) {
            types.add(type);
        }

        public void addMethod(String type, String method) {
            methods.add(type + "#" + method);
        }

        public void addCall(String caller, String callee) {
            calls.add(caller + " -> " + callee);
        }

        public ToyFacts build() {
            return new ToyFacts(List.copyOf(types), List.copyOf(methods),
                    List.copyOf(calls));
        }
    }
}
