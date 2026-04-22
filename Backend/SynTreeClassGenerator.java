package tools;

import java.util.List;
import java.util.Arrays;
import java.io.PrintWriter;
import java.io.IOException;


//automates programmer Syntax Tree class creation
public class SynTreeClassGenerator {
    public static void main(String[] argc) throws IOException{
        if(argc.length != 1) {
            System.err.println("Expected usage: generate syntax_tree output directory");
            System.exit(65);
        }
        String outputDir = argc[0];

        //creates Expression.java
        defineClass(outputDir, "Expression",
                Arrays.asList(
                        "Binary     : Expression left, Token operator, Expression right",
                        "Unary      : Token operator, Expression left",
                        "Grouping   : Expression expression",
                        "Literal    : Object value"

        ));
    }

    private static void defineClass(String outDir, String baseName, List<String> subclasses)
                        throws IOException {

        String path = outDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path);

        //defines file
        writer.println("package interpreter;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + "{");

        defineVisitor(writer, baseName, subclasses);
        writer.println();
        writer.println("    abstract <R> R accept(Visitor<R> visitor);");
        writer.println();


        //defines subclass types
        for(String subclass : subclasses){
            String[] parsed = subclass.split(":");
            String subclassName = parsed[0].trim();
            String fieldList = parsed[1].trim();
            defineSubClass(writer, baseName, subclassName, fieldList);
        }

        writer.println("}");
        writer.close();
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> subclasses){
        writer.println("    interface Visitor<R>{");
        for(String subclass : subclasses){
            String[] parsed = subclass.split(":");
            String subclassName = parsed[0].trim();
            writer.println("        R visit" + subclassName + baseName +
                            "(" + subclassName + " " + baseName.toLowerCase() + ");");
        }
        writer.println("    }");
    }

    private static void defineSubClass(PrintWriter writer, String baseName, String subclassName, String fieldList){
        writer.println("    static class " + subclassName + " extends " + baseName + "{");
        String[] fields = fieldList.split(", ");

        //fields
        for(String field : fields){
            writer.println("        final " + field + ";");
        }
        writer.println();

        //constructor
        writer.println("        " + subclassName + "(" + fieldList + "){");
        for(String field : fields){
            String[] expression = field.split(" ");
            String fieldName = expression[1];
            writer.println("                this." + fieldName + " = " + fieldName + ";");
        }
        writer.println("        }");
        //end constructor

        writer.println();
        writer.println("        @Override");
        writer.println("        <R> R accept(Visitor<R> visitor){");
        writer.println("            return visitor.visit" + subclassName + baseName + "(this);");
        writer.println("        }");
        writer.println("    }");
        //end of subclass

        writer.println();
    }

}
