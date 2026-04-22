package interpreter;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.List;

public class Interpreter{
    static boolean hadError = false;

    public static void main(String[] argc) throws IOException{
        if(argc.length > 1){
            throw new RuntimeException("too many args");
        }

        //User file
        else if(argc.length == 1){
            runFile(argc[0]);
            System.exit(64);
        }

        //User input
        else if(argc.length == 0){
            runPrompt();
            System.exit(64);
        }
    }

    //runs file
    private static void runFile(String path) throws IOException{
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String os_string = new String(bytes, Charset.defaultCharset());
        run(os_string);

        if (hadError) System.exit(65);
    }

    //runs prompts line by line
    private static void runPrompt() throws IOException{
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        while(true){
            System.out.print("> ");
            String line = reader.readLine();
            if(line == null) break;
            run(line);
            hadError = false;
        }
    }

    //tokenizes input
    //IMPORTANT: For now, print tokens back
    private static void run(String source_string){
        Lexer lexer = new Lexer(source_string);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        Expression expr = parser.parse();
        if(hadError) return;

        System.out.println(new SyntaxTreePrint().print(expr));
    }



    static void error(int line, String message){
        report(line, "", message);
    }
    static void error(Token token, String message){
        if(token.type == TokenType.END_OF_FILE) report(token.line, " at end", message);
        else report(token.line, " at '" + token.lexeme + "'", message);
    }

    private static void report(int line, String location, String message){
        System.err.print("[line: " + line + "] Error" + location +": " + message);
        hadError = true;
    }


}