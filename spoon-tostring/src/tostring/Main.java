package tostring;

import java.nio.file.Files;
import java.nio.file.Path;

import spoon.Launcher;

/**
 * Lance la transformation : java tostring.Main DOSSIER_SOURCES DOSSIER_SORTIE
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        if (args.length != 2 || !Files.isDirectory(Path.of(args[0]))) {
            System.err.println("Usage : java tostring.Main DOSSIER_SOURCES DOSSIER_SORTIE");
            System.exit(2);
        }
        Launcher launcher = new Launcher();
        launcher.addInputResource(args[0]);
        launcher.setSourceOutputDirectory(args[1]);
        launcher.getEnvironment().setComplianceLevel(17);
        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setCommentEnabled(true);
        launcher.addProcessor(new ToStringGenerator());
        launcher.run(); // construit le modèle, applique le processeur, écrit le code transformé
        System.out.println("Code transforme ecrit dans " + Path.of(args[1]).toAbsolutePath());
    }
}
