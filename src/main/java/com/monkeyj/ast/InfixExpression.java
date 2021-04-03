package com.monkeyj.ast;

import com.monkeyj.token.Token;

public class InfixExpression implements Expression, Node {

    private Token token;
    private Expression left;
    private String operator;
    private Expression right;

    @Override
    public void expressionNode() { }

    @Override
    public String tokenLiteral() {
        return this.token.literal();
    }

    public InfixExpression() {}

    public InfixExpression(final Token token, final Expression left, final String operator, final Expression right) {
        this.token = token;
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toString() {
        final StringBuilder out = new StringBuilder();

        out.append("(").append(this.left.toString()).append(" ").append(this.operator).append(" ");
        out.append(this.right.toString());
        out.append(")");

        return out.toString();
    }

    public Token getToken() {
        return token;
    }

    public void setToken(final Token token) {
        this.token = token;
    }

    public Expression getLeft() {
        return left;
    }

    public void setLeft(final Expression left) {
        this.left = left;
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
}
