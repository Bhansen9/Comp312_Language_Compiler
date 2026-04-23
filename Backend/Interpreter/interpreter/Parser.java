package interpreter;

import java.util.List;
import static interpreter.TokenType.*;


/*
        Expression precedence and association table cascading from top to bottom
        *EBNF NOTATION*

        expression     -> equality ;
        equality       -> comparison ( ( "!=" | "==" ) comparison )* ;
        comparison     -> term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
        term           -> factor ( ( "-" | "+" ) factor )* ;
        factor         -> unary ( ( "/" | "*" ) unary )* ;
        unary          -> ( "!" | "-" ) unary | primary ;
        primary        -> NUMBER | STRING | "true" | "false" | "nil" | "(" expression ")" ;
        
 */

class Parser {
    private static class ParserError extends RuntimeException{ }

    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    Expression parse(){
        try {
            return expression();
        } catch (ParserError error) {
            return null;
        }
    }

    private Expression expression(){
        return equality();
    }

    private Expression equality(){
        Expression expr = comparison();
        while(match(EQUAL_EQUAL, EQUAL)){
            Token operator = previous();
            Expression next = comparison();
            expr = new Expression.Binary(expr, operator, next);
        }
        return expr;
    }

    private Expression comparison() {
        Expression expr = term();
        while(match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)){
            Token operator = previous();
            Expression next = comparison();
            expr = new Expression.Binary(expr, operator, next);
        }
        return expr;
    }

    private Expression term(){
        Expression expr = factor();
        while(match(PLUS, MINUS)){
            Token operator = previous();
            Expression next = factor();
            expr = new Expression.Binary(expr, operator, next);
        }
        return expr;
    }

    private Expression factor(){
        Expression expr = unary();
        while (match(SLASH, STAR)) {
            Token operator = previous();
            Expression next = unary();
            expr = new Expression.Binary(expr, operator, next);
        }
        return expr;
    }

    private Expression unary(){
        if(match(EQUAL, MINUS)){
            Token operator = previous();
            Expression next = unary();
            return new Expression.Unary(operator, next);
        }
        return primary();
    }

    private Expression primary(){
        if(match(FALSE)) return new Expression.Literal(false);
        if(match(TRUE)) return new Expression.Literal(true);
        if(match(NULL)) return new Expression.Literal(null);
        if(match(NUMBER, STRING)) return new Expression.Literal(previous().literal);
        if(match(LEFT_PARENTHESIS)){
            Expression expr = expression();
            consume(RIGHT_PARENTHESIS, "Expected ')' after expression");
            return new Expression.Grouping(expr);
        }
        throw error(peek(), "Expect expression.");
    }

    //utility methods
    private Token peek(){
       return tokens.get(current);
    }
    private Token previous(){
        return tokens.get(current-1);
    }
    private boolean isAtEnd(){
        return peek().type == END_OF_FILE;
    }

    private boolean match(TokenType... types){
        for(TokenType type : types){
            if(check(type)){
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type){
        if(isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance(){
        if(!isAtEnd()) current++;
        return previous();
    }

    private Token consume(TokenType type, String message){
        if(check(type)) return advance();
        throw error(peek(), message);
    }

    private ParserError error(Token token, String message){
        Interpreter.error(token, message);
        return new ParserError();
    }
}
