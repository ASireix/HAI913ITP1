# HAI913I — TP1 Analyse statique — squelette de départ

Squelette Maven **facultatif** pour démarrer le TP1. Il ne contient **aucune** partie évaluée :
ni extraction de structure, ni appels, ni métriques, ni graphe d'appel. Il fournit seulement un
environnement qui compile, une configuration `ASTParser` qui résout les bindings, et un test de
fumée correspondant au **point de contrôle A0**.

Vous pouvez aussi partir de zéro : dans ce cas, reprenez au minimum les réglages de
`JdtParser` et vérifiez-les avec le projet de validation.

## Versions testées

| Élément          | Version                                             |
|------------------|-----------------------------------------------------|
| JDK              | 17 (minimum) — testé avec 17.0.10 et 21.0.5         |
| Maven            | 3.9.6                                               |
| Eclipse JDT Core | `org.eclipse.jdt:org.eclipse.jdt.core:3.46.0`       |
| JUnit Jupiter    | 5.14.4                                              |

## Commandes

A lancer depuis le dossier starter.

```text
mvn clean test          # compile et lance le test de fumée (A0)
mvn clean package       # produit target/hai913i-tp1-analyzer.jar
java -jar target/hai913i-tp1-analyzer.jar ../resources/validation
```

Sous Windows, entourez de guillemets les chemins qui contiennent des espaces :

```text
java -jar target\hai913i-tp1-analyzer.jar "C:\Mes cours\HAI913I\validation"
```

Sortie attendue sur le projet de validation :

```text
Racine des sources     : ...\validation\src
Unites de compilation  : 14
Erreurs de compilation : 0
```

## Organisation

```text
src/main/java/hai913i/tp1/Main.java               point d'entrée (lit les arguments, délègue)
src/main/java/hai913i/tp1/parse/ProjectSources.java   racine des sources et fichiers .java
src/main/java/hai913i/tp1/parse/JdtParser.java        configuration d'ASTParser (bindings)
src/test/java/hai913i/tp1/ParserSmokeTest.java        point de contrôle A0
src/test/resources/sample/                            mini-projet de deux fichiers
```

Ajoutez vos propres paquetages (par exemple `model`, `extraction`, `metrics`, `callgraph`,
`report`) : l'organisation est libre, mais elle doit rester lisible et justifiée dans le compte rendu.

Ce `README.md` décrit le squelette ; le README demandé dans le livrable est **le vôtre** et doit
décrire votre analyseur (voir le sujet, section « Livrables »).

## Affichage de la partie A

Depuis `starter`, avec un JDK 17 ou plus :

```bash
mvn package
java -jar target/hai913i-tp1-analyzer.jar ../validation lite
```

Le programme affiche A0 (fichiers et erreurs), A1 (AST de Dvd et Category),
A2 (structure et bilan par type), A3 (appels par méthode), puis les totaux.
Omettre `lite` affiche les AST complets en A1.

Les visiteurs `JdtVisitorClasses` et `JdtVisitorMethods` collectent les données
`AnalysisFacts`. `report/ConsoleReporter` assure leur affichage sans dépendre de
JDT. Ses méthodes `displaySummary`, `displayStructure`, `displayCalls` et
`displayTotals` peuvent être appelées séparément. L'affichage des cibles conserve
la forme `Type#methode()` ; les identifiants complets sont stockés séparément dans le modèle pour B3.

Les tests de la partie A utilisent aussi les dossiers voisins `validation` et
`robustness` : conserver l'organisation du TP pour les exécuter.

## Modèle B1

`FactExtractor.extract` construit un `ProjectFacts` à partir des AST déjà analysés.
Il contient les chemins relatifs et lignes physiques des fichiers, les types et
les diagnostics. `methodsById()` donne un index des méthodes et `calls()` la liste
des appels, sans utiliser JDT.

Les identifiants suivent `typeQualifie#nom(typesParametres)` ; les constructeurs
utilisent `<init>`. Déclarations et cibles passent par `MethodIdentity`, qui ramène
les bindings à leur déclaration puis efface les types génériques. Chaque appel
conserve `callerId` et un `Optional<String> targetId` vide si non résolu.
`MethodFact` conserve aussi les lignes du corps (zéro si absent). Les fichiers,
types et méthodes sont triés ; les appels gardent l'ordre du parcours source.
Les listes du modèle sont non modifiables.

`ProjectFactsTest` vérifie les liens internes, les surcharges, les appels non
résolus et l'égalité du modèle obtenu par deux analyses indépendantes. B2 et B3
consomment ce modèle uniquement.

## Métriques B2

Les treize métriques sont affichées après la partie A. Le seuil X vaut 5 par défaut :

```bash
java -jar target/hai913i-tp1-analyzer.jar ../validation lite --threshold 4
```

`MetricsService` calcule exclusivement sur `ProjectFacts`, renvoie un
`MetricsResult` et ne dépend pas de JDT. `ConsoleReporter.displayMetrics` affiche
les résultats avec deux décimales pour les moyennes. Les classements prennent
le plafond de 10 % des candidats, gardent les ex æquo et excluent les zéros.
Un modèle vide donne des moyennes à zéro et des classements vides.
`MetricsTest` vérifie le point de contrôle B2 et ces cas limites.

## Graphe d'appel B3

Le graphe texte liste toutes les méthodes (y compris isolées), les arcs pondérés
et les totaux. Les constructeurs sont des nœuds ; leurs appels ne sont pas ajoutés.
Exemples de recherches, depuis `starter` :

```bash
java -jar target/hai913i-tp1-analyzer.jar ../validation lite --callers 'library.model.Loanable#checkOut(library.model.Member)'
java -jar target/hai913i-tp1-analyzer.jar ../validation lite --callees 'library.service.Catalog#add(library.model.Item,library.model.Item)'
```

Les deux options peuvent être combinées. Une méthode inconnue produit un message
et le code de sortie 3. `CallGraphService` construit le graphe uniquement à partir
des faits ; `CallGraph.callers` et `callees` exposent les deux sens de recherche.
Les identifiants et arcs sont triés pour une sortie stable. Le format texte permet
la lecture directe et la redirection vers un fichier sans bibliothèque graphique.

Résultats attendus : 59 nœuds, 44 arcs, 46 sites internes, 48 appels externes,
0 non résolu. `CallGraphTest` couvre les comptages, le poids 2 entre les surcharges
de `add`, la récursion, les recherches inverses, le graphe vide et `missing-dep`.

## Interface finale B4

```bash
java -jar target/hai913i-tp1-analyzer.jar --help
java -jar target/hai913i-tp1-analyzer.jar ../validation --action structure
java -jar target/hai913i-tp1-analyzer.jar ../validation --action calls
java -jar target/hai913i-tp1-analyzer.jar ../validation --action metrics --threshold 5
java -jar target/hai913i-tp1-analyzer.jar ../validation --action graph --callers 'library.model.Loanable#checkOut(library.model.Member)'
java -jar target/hai913i-tp1-analyzer.jar ../validation --action ast --lite
java -jar target/hai913i-tp1-analyzer.jar ../validation --action all lite
```

`all` est l'action par défaut. `summary` affiche seulement le bilan A0, présent
aussi avant les autres actions. `ast` parcourt tous les fichiers ; dans `all`,
A1 reste limité à Dvd et Category. Les recherches sont réservées à `graph` et
`all`. X vaut 5 si l'option est omise ; `--threshold` sans valeur est une erreur.

Codes de sortie : 0 = analyse réalisée (éventuellement partielle avec diagnostics),
2 = arguments invalides, 3 = entrée inexploitable ou méthode inconnue,
4 = autre exception empêchant l'analyse. Les messages ne montrent pas de pile
Java brute. Une erreur de compilation dans les sources analysées n'est pas un
échec du lancement : `syntax-error` continue avec Helper et `missing-dep` conserve
les appels non résolus.

### Construction depuis une copie propre

Conserver les dossiers voisins `starter`, `validation` et `robustness`, utilisés
par les tests. Depuis `starter` :

```bash
java -version
mvn -version
mvn clean package
java -jar target/hai913i-tp1-analyzer.jar "../validation" --action all lite
```

Environnement vérifié : Arch Linux x86_64, OpenJDK 21.0.12.1, Maven 3.9.16,
JDT Core 3.46.0, outils en ligne de commande. Le code cible Java 17.
Maven nécessite un accès aux dépôts pour les dépendances absentes du cache.
La reconstruction a aussi été vérifiée avec `mvn -o clean package` (dépendances
locales disponibles), dans une copie sans `target/` sous un chemin avec espaces.

Pour vérifier les erreurs :

```bash
java -jar target/hai913i-tp1-analyzer.jar ../robustness/no-java-files
java -jar target/hai913i-tp1-analyzer.jar ../robustness/syntax-error --action structure
java -jar target/hai913i-tp1-analyzer.jar ../robustness/missing-dep --action calls
java -jar target/hai913i-tp1-analyzer.jar ../validation --threshold abc
```

Si Maven signale l'absence de compilateur, sélectionner un JDK avec `JAVA_HOME`
et vérifier `mvn -version`. Entourer de guillemets les chemins contenant des
espaces. Les bibliothèques externes absentes du projet analysé restent signalées
par des diagnostics et des bindings non résolus.
