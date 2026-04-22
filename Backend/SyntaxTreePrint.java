package interpreter;

class SyntaxTreePrint implements Expression.Visitor<String>{

    String print(Expression expr){
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpression(Expression.Binary expr){
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitUnaryExpression(Expression.Unary expr){
        return parenthesize(expr.operator.lexeme, expr.left);
    }


    @Override
    public String visitGroupingExpression(Expression.Grouping expr){
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpression(Expression.Literal expr){
        if(expr.value == null) return "null";
        return expr.value.toString();
    }

    private String parenthesize(String name, Expression... expr){
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append(name);
        for(Expression e : expr){
            builder.append(" ");
            builder.append(e.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }

    public static void main(String[] argc) {
        Expression expression = new Expression.Binary(
                new Expression.Unary(
                        new Token(TokenType.MINUS, null, "-", 1),
                        new Expression.Literal(123)),
                new Token(TokenType.STAR, null, "*", 1),
                new Expression.Grouping(
                        new Expression.Literal(45.67)));

        System.out.println(new SyntaxTreePrint().print(expression));
    }

}
