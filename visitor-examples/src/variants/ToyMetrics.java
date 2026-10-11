package variants;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Un traitement, comme B2 ou B3 : il ne lit que le modèle de faits et
 * n'importe aucune classe de JDT.
 */
public final class ToyMetrics {

    private ToyMetrics() {
    }

    public static Map<String, Long> methodsPerType(ToyFacts facts) {
        return facts.methods().stream().collect(Collectors.groupingBy(
                id -> id.substring(0, id.indexOf('#')),
                TreeMap::new, Collectors.counting()));
    }
}
