package com.monkeyj.ast;

import com.monkeyj.token.Token;

import java.util.Objects;

public class LetStatement implements Statement {

    private Token token;
    private Identifier name;
    private Expression value;

    @Override
    public String tokenLiteral() {
        return this.token.literal();
    }

    @Override
    public void statementNode() {

    }

    public Token getToken() {
        return token;
    }

    public void setToken(final Token token) {
        this.token = token;
    }

    public Identifier getName() {
        return name;
    }

    public void setName(final Identifier name) {
        this.name = name;
    }

    public Expression getValue() {
        return value;
    }

    public void setValue(final Expression value) {
        this.value = value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LetStatement that = (LetStatement) o;
        return Objects.equals(token, that.token) && Objects.equals(name, that.name) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, name, value);
    }

    @Override
    public String toString() {
        final StringBuilder out = new StringBuilder();

        out.append(this.tokenLiteral()).append(" ");
        out.append(this.getName().toString());
        out.append(" = ");

        if (this.value != null) {
            out.append(this.value.toString());
        }

        out.append(";");

        return out.toString();
    }
}
