package com.monkeyj.ast;

import com.monkeyj.token.Token;

import java.util.Objects;

public class ExpressionStatement implements Statement {
    private Token token;
    private Expression expression;

    @Override
    public String tokenLiteral() {
        return this.token.literal();
    }

    @Override
    public void statementNode() { }

    public Token getToken() {
        return token;
    }

    public void setToken(final Token token) {
        this.token = token;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(final Expression expression) {
        this.expression = expression;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExpressionStatement that = (ExpressionStatement) o;
        return Objects.equals(token, that.token) && Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, expression);
    }

    @Override
    public String toString() {
        if (this.expression != null) {
            return this.expression.toString();
        }
        return "x";
    }
}
