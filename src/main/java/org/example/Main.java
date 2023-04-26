package org.example;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.printer.PrettyPrinter;
import com.github.javaparser.printer.configuration.Indentation;
import com.github.javaparser.printer.configuration.PrettyPrinterConfiguration;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final String FILE_PATH = "src/main/java/org/example/ReversePolishNotation.java";

    private static class CommentReportEntry {
        private String type;
        private String text;
        private int lineNumber;
        private boolean isOrphan;

        public CommentReportEntry(String type, String text, int lineNumber, boolean isOrphan) {
            this.type = type;
            this.text = text;
            this.lineNumber = lineNumber;
            this.isOrphan = isOrphan;
        }

        @Override
        public String toString() {
            return lineNumber + "|" + type + "|" + isOrphan + "|" + text.replaceAll("\\n", "").trim();
        }
    }

    public static void main(String[] args) throws Exception {
        //disables counting comments while parsing
//        StaticJavaParser.setConfiguration(new ParserConfiguration().setAttributeComments(false));


        //do not count:

//        >>>>>  bla bla  <<<<<<<<<

        //void sayHello
//        StaticJavaParser.setConfiguration(new ParserConfiguration().setDoNotAssignCommentsPrecedingEmptyLines(false));

        CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));
        VoidVisitor<Void> methodNameVisitor = new MethodNamePrinter();
        methodNameVisitor.visit(cu, null);
        System.out.println();

        List<String> mathodNames = new ArrayList<>();
        VoidVisitor<List<String>> methodNameCollector = new MethodNameCollector();
        methodNameCollector.visit(cu, mathodNames);
        mathodNames.forEach(n -> System.out.println("Method name collected: " + n));
        System.out.println();

        System.out.println(cu.toString());
        System.out.println("#########");
        ModifierVisitor<?> numericalLiteralVisitor = new IntegerLiteralModifier();
        numericalLiteralVisitor.visit(cu, null);

        System.out.println(cu.toString());
        System.out.println();


        List<CommentReportEntry> comments = cu.getAllContainedComments().stream().map(c ->
                new CommentReportEntry(c.getClass().getSimpleName(),
                        c.getContent(),
                        c.getRange().map(r -> r.begin.line).orElse(-1),
                        !c.getCommentedNode().isPresent())).collect(Collectors.toList());

        comments.forEach(System.out::println);
        System.out.println();

        ClassOrInterfaceDeclaration class1 = new ClassOrInterfaceDeclaration();
        class1.setComment(new LineComment("This is my comment!"));
        class1.setName("Book");
        class1.addField("String", "name");

        PrettyPrinter prettyPrinter = new PrettyPrinter();
        PrettyPrinterConfiguration conf = new PrettyPrinterConfiguration();
        conf.setIndentation(new Indentation(Indentation.IndentType.SPACES));
//        conf.setPrintComments(false);

        prettyPrinter.setConfiguration(conf);

        System.out.println(prettyPrinter.print(class1));
        System.out.println();

        String myCode = "//This is my comment \n class Ball { }";
        CompilationUnit cu2 = StaticJavaParser.parse(myCode);
        LexicalPreservingPrinter.setup(cu2);
        System.out.println(LexicalPreservingPrinter.print(cu2));


    }
}