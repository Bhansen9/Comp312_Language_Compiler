package interpreter;

public class Token {
    final TokenType type;
    final int line;
    final Object literal;
    final String lexeme;

    public Token(TokenType type, Object literal, String lexeme, int line){
        this.type = type;
        this.literal = literal;
        this.lexeme = lexeme;
        this.line = line;
    }

    @Override
    public String toString(){
        return type + " " + lexeme + " " + literal;
    }
}
