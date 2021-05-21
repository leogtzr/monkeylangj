package com.monkeyj.ast;

import com.monkeyj.token.Token;

import java.util.Objects;

public class IndexExpression implements Node, Expression {

    private Token token;
    private Expression left;
    private Expression index;

    public IndexExpression() {}

    public IndexExpression(final Token token, final Expression left, final Expression index) {
        this.token = token;
        this.left = left;
        this.index = index;
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

    public Expression getIndex() {
        return index;
    }

    public void setIndex(final Expression index) {
        this.index = index;
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
        out.append(this.left.toString());
        out.append("[");
        out.append(this.index.toString());
        out.append("])");

        return out.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final IndexExpression that = (IndexExpression) o;
        return Objects.equals(token, that.token) && Objects.equals(left, that.left) && Objects.equals(index, that.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, left, index);
    }
}
