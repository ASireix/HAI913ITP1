# HAI913I — TP 1 — Exemples exécutables de l'annexe 3 (Visitor)

Projet Maven compagnon de l'annexe 3 « Le patron Visitor, ASTVisitor et Spoon ».
Les listings de l'annexe sont inclus depuis ces fichiers, compilés et exécutés par la QA
(JDK 17.0.10 et 21.0.5, Maven 3.9.6, JDT Core 3.46.0, Spoon 11.5.0).

## Contenu

| Paquetage | Rôle |
|---|---|
| `gof` | patron Visitor complet sur l'arbre `2 * (3 + 4)` : `Expr`, `Num`, `Add`, `Mul`, `ExprVisitor<R>`, `Evaluator`, `Printer`, `TracingEvaluator`, `GofDemo` |
| `jdt` | `ASTVisitor` de JDT : ordre de visite et élagage (`JdtVisitorDemo`), trace `visit`/`endVisit` (`JdtTraceDemo`) |
| `variants` | six façons d'utiliser un visiteur dans la chaîne d'analyse du TP (modèle de faits « jouet ») |
| `spoondemo` | scanner, filtre et processeur Spoon (bonus) |

## Commandes

```text
mvn -q clean compile
mvn -q exec:java -Dexec.mainClass=gof.GofDemo
mvn -q exec:java -Dexec.mainClass=jdt.JdtVisitorDemo
mvn -q exec:java -Dexec.mainClass=jdt.JdtTraceDemo
mvn -q exec:java -Dexec.mainClass=variants.VariantsDemo
mvn -q exec:java -Dexec.mainClass=spoondemo.SpoonDemo
```

Les visiteurs de `variants` sont volontairement simplifiés (pas d'énumérations, de
types imbriqués ni de bindings) : ils illustrent l'architecture, pas les étapes A2 et A3.
