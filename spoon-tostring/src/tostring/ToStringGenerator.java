package tostring;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtComment;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.factory.Factory;

/**
 * Processeur Spoon : ajoute toString() aux classes qui n'en ont pas et dont au moins un attribut
 * possède un accesseur getXxx() sans paramètre.
 */
public class ToStringGenerator extends AbstractProcessor<CtClass<?>> {

    @Override
    public boolean isToBeProcessed(CtClass<?> candidate) {
        boolean hasToString = candidate.getMethods().stream()
                .anyMatch(m -> m.getSimpleName().equals("toString") && m.getParameters().isEmpty());
        return !hasToString && !getters(candidate).isEmpty();
    }

    @Override
    public void process(CtClass<?> type) {
        Factory factory = getFactory();
        String body = getters(type).stream()
                .map(getter -> "\"" + fieldName(getter) + "=\" + " + getter.getSimpleName() + "()")
                .collect(Collectors.joining(" + \", \" + ", "return \"" + type.getSimpleName() + "[\" + ", " + \"]\""));

        CtMethod<String> toString = factory.createMethod();
        toString.setSimpleName("toString");
        toString.setType(factory.Type().stringType());
        toString.addModifier(ModifierKind.PUBLIC);
        toString.addAnnotation(factory.createAnnotation(factory.Type().createReference(Override.class)));
        toString.setBody(factory.Code().createCodeSnippetStatement(body));
        toString.addComment(factory.Code().createComment("Genere par Spoon (HAI913I)", CtComment.CommentType.INLINE));
        type.addMethod(toString);
        System.out.println("toString() ajoutee a " + type.getQualifiedName());
    }

    /** Accesseurs getXxx() sans paramètre qui correspondent à un attribut xxx de la classe. */
    static List<CtMethod<?>> getters(CtClass<?> type) {
        return type.getMethods().stream()
                .filter(m -> m.getSimpleName().matches("get[A-Z]\\w*") && m.getParameters().isEmpty())
                .filter(m -> {
                    CtField<?> field = type.getField(fieldName(m));
                    return field != null;
                })
                .sorted((a, b) -> a.getSimpleName().compareTo(b.getSimpleName()))
                .collect(Collectors.toList());
    }

    static String fieldName(CtMethod<?> getter) {
        String suffix = getter.getSimpleName().substring(3);
        return suffix.substring(0, 1).toLowerCase(Locale.ROOT) + suffix.substring(1);
    }
}
