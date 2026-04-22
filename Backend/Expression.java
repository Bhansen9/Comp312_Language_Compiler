package interpreter;

import java.util.List;

/*
        This class serves as parser's format layout.
        It explains to the parser how an expression is formatted
        *EBNF NOTATION*

        expression     -> literal | unary | binary | grouping;
        literal        -> STRING | NUMBER | "true" | "false" | "null";
        unary          -> ("!" | "=") expression;
        binary         -> expression operator expression;
        operator       -> "==" | "!=" | "<" | "<=" | ">" | ">="
                          | "+"  | "-"  | "*" | "/" ;
 */
abstract class Expression{
    interface Visitor<R>{
        R visitBinaryExpression(Binary expression);
        R visitUnaryExpression(Unary expression);
        R visitGroupingExpression(Grouping expression);
        R visitLiteralExpression(Literal expression);
    }

    abstract <R> R accept(Visitor<R> visitor);

    static class Binary extends Expression{
        final Expression left;
        final Token operator;
        final Expression right;

        Binary(Expression left, Token operator, Expression right){
                this.left = left;
                this.operator = operator;
                this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor){
            return visitor.visitBinaryExpression(this);
        }
    }

    static class Unary extends Expression{
        final Token operator;
        final Expression left;

        Unary(Token operator, Expression left){
                this.operator = operator;
                this.left = left;
        }

        @Override
        <R> R accept(Visitor<R> visitor){
            return visitor.visitUnaryExpression(this);
        }
    }

    static class Grouping extends Expression{
        final Expression expression;

        Grouping(Expression expression){
                this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor){
            return visitor.visitGroupingExpression(this);
        }
    }

    static class Literal extends Expression{
        final Object value;

        Literal(Object value){
                this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor){
            return visitor.visitLiteralExpression(this);
        }
    }

}
