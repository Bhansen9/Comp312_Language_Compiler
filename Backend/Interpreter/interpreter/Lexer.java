package interpreter;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import static interpreter.TokenType.*;

class Lexer{

    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0; //walks with current for lexeme purposes
    private int current = 0;
    private int line = 1;
    private static final Map<String, TokenType> reserved_keywords = new HashMap<>();
    static{
        reserved_keywords.put("and", AND);
        reserved_keywords.put("or", OR);
        reserved_keywords.put("else", ELSE);
        reserved_keywords.put("if", IF);
        reserved_keywords.put("false", FALSE);
        reserved_keywords.put("true", TRUE);
        reserved_keywords.put("while", WHILE);
        reserved_keywords.put("for", FOR);
        reserved_keywords.put("null", NULL);
        reserved_keywords.put("print", PRINT);
        reserved_keywords.put("return", RETURN);
        reserved_keywords.put("super", SUPER);
        reserved_keywords.put("this", THIS);
        reserved_keywords.put("var", VAR);
        reserved_keywords.put("function", FUNCTION);
        reserved_keywords.put("class", CLASS);
    }

    Lexer(String source){
        this.source = source;
    }

    //initializes Parser's token list
    List<Token> scanTokens() {
        while (!isAtSourceEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(END_OF_FILE, "", null, line));
        return tokens;
    }
    private void scanToken(){
        char c = next_char();
        switch(c){
            //single character tokens
            case('('): addToken(LEFT_PARENTHESIS); break;
            case(')'): addToken(RIGHT_PARENTHESIS); break;
            case('{'): addToken(LEFT_BRACKET); break;
            case('}'): addToken(RIGHT_BRACKET); break;
            case('+'): addToken(PLUS); break;
            case('-'): addToken(MINUS); break;
            case('.'): addToken(DOT); break;
            case(','): addToken(COMMA); break;
            case(';'): addToken(SEMICOLON); break;
            case('*'): addToken(STAR); break;
            case('/'):
                if(match('/')){
                    //comment detected. No need to tokenize commented strings
                    while(lookahead() != '\n' && !isAtSourceEnd()) next_char();
                }
                else{
                    addToken(SLASH);
                }
                break;

            //logical processes
            case('!'):
                addToken(match('=') ? EXCLAMATION_EQUAL : EXCLAMATION); break;
            case('='):
                addToken(match('=') ? EQUAL_EQUAL : EQUAL); break;
            case('>'):
                addToken(match('=') ? GREATER_EQUAL : GREATER); break;
            case('<'):
                addToken(match('=') ? LESS_EQUAL : LESS); break;

            case(' '):
            case('\t'):
                break;

            case('\n'):
                line++;
                break;

            //string token creation
            case('"'):
                string();
                break;

            default:
                if(isDigit(c)) {
                    number();
                }
                else if(isAlpha(c)) {
                    identifier();
                }
                else {
                    Interpreter.error(line, "Invalid character");
                }
                break;
        }
    }

    private boolean isAtSourceEnd(){
        return (current >= source.length());
    }

    //looks at current char
    //advances current
    private char next_char(){
        return source.charAt(current++);
    }

    //
    private boolean match(char expected){
        if(isAtSourceEnd()) return false;
        if(source.charAt(current) != expected) return false;

        current++;
        return true;
    }


    private char lookahead(){
        if(isAtSourceEnd()) return '\0'; //return null;
        return source.charAt(current);
    }

    private char lookaheadNext(){
        if(current + 1 >= source.length()) return '\n';
        return source.charAt(current+1);
    }
    private void string(){
        while(lookahead() != '"' && !isAtSourceEnd()){
            if(lookahead() == '\n') line++;
            next_char();
        }

        if(isAtSourceEnd()){
            Interpreter.error(line, "Invalid String");
        }

        next_char();

        addToken(STRING, source.substring(start+1, current-1));
    }

    private boolean isDigit(char c){
        return c >= '0' && c <= '9';
    }

    //interpreter only supports double representation
    private void number(){
        while(isDigit(lookahead()) && !isAtSourceEnd()) next_char();
        if(lookahead() == '.' && isDigit(lookaheadNext())) {
            next_char();
            while(isDigit(lookahead())) next_char();
        }
        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private boolean isAlpha(char c){
        return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_');
    }

    private boolean isAlphaNumeric(char c){
        return isAlpha(c) || isDigit(c);
    }

    private void identifier(){
        while(isAlphaNumeric(lookahead())) next_char();
        String identified = source.substring(start, current);
        TokenType type = reserved_keywords.get(identified);
        if(type == null) type = IDENTIFIER;
        addToken(type);
    }

    private void addToken(TokenType type){
        addToken(type, "");
    }

    private void addToken(TokenType type, Object literal){
        tokens.add(new Token(type,
                    literal,
                    source.substring(start, current),
                    line));
    }

}