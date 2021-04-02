package com.monkeyj.ast;

import com.monkeyj.token.Token;

import java.util.Objects;

public class IntegerLiteral implements Expression, Node {

    private Token token;
    private Integer value;

    public IntegerLiteral() {}

    public IntegerLiteral(final Token token, final Integer value) {
        this.token = token;
        this.value = value;
    }

    @Override
    public void expressionNode() { }

    @Override
    public String tokenLiteral() {
        return this.token.literal();
    }

    public Token getToken() {
        return token;
    }

    public void setToken(final Token token) {
        this.token = token;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(final Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.token.literal();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final IntegerLiteral that = (IntegerLiteral) o;
        return Objects.equals(token, that.token) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, value);
    }
}
