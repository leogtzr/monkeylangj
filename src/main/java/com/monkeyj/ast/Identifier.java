package com.monkeyj.ast;

import com.monkeyj.token.Token;

import java.util.Objects;

public class Identifier implements Expression, Node {

    private Token token;
    private String value;

    public Identifier(Token token, String value) {
        this.token = token;
        this.value = value;
    }

    public Identifier() {}

    public Token getToken() {
        return token;
    }

    public void setToken(final Token token) {
        this.token = token;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    public void expressionNode() {
    }

    @Override
    public String tokenLiteral() {
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
        final Identifier that = (Identifier) o;
        return Objects.equals(token, that.token) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, value);
    }
}
