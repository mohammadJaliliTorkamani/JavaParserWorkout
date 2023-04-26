package org.example;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

import java.util.regex.Pattern;

public class IntegerLiteralModifier extends ModifierVisitor<Void> {
    private static final Pattern LOOK_AHEAD_TREE = Pattern.compile("(\\d)(?=(\\d{3})+$)");

    @Override
    public Visitable visit(FieldDeclaration fd, Void arg) {
        super.visit(fd, arg);
        fd.getVariables().stream().forEach(v -> {
            v.getInitializer().ifPresent(i -> {
                i.ifIntegerLiteralExpr(il -> {
                    v.setInitializer(formatWithUnderscores(il.getValue()));
                });
            });
        });
        return fd;
    }

    static String formatWithUnderscores(String value) {
        String withoutUnderscores = value.replaceAll("_", "");
        return LOOK_AHEAD_TREE.matcher(withoutUnderscores).replaceAll("$1_");
    }
}
