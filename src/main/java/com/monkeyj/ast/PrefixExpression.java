package com.monkeyj.ast;

import com.monkeyj.token.Token;

public class PrefixExpression implements Expression, Node {

    private Token token;
    private String operator;
    private Expression right;

    public PrefixExpression() {}

    public PrefixExpression(final Token token, final String operator, final Expression right) {
        this.token = token;
        this.operator = operator;
        this.right = right;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(final Token token) {
        this.token = token;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(final String operator) {
        this.operator = operator;
    }

    public Expression getRight() {
        return right;
    }

    public void setRight(final Expression right) {
        this.right = right;
    }

    @Override
    public void expressionNode() { }

    @Override
    public String tokenLiteral() {
        return this.token.literal();
    }

    @Override
    public String toString() {
        final StringBuilder out = new StringBuilder();

        out.append("(");
        out.append(this.operator);
        out.append(this.right.toString());
        out.append(")");

        return out.toString();
    }
}
