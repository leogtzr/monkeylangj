package com.monkeyj.ast;

import com.monkeyj.token.Token;

public class Bool implements Expression, Node {

    private Token token;
    private boolean value;

    public Bool() {}

    public Bool(final Token token, final boolean value) {
        this.token = token;
        this.value = value;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(final Token token) {
        this.token = token;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(final boolean value) {
        this.value = value;
    }

    @Override
    public void expressionNode() { }

    @Override
    public String tokenLiteral() {
        return this.token.literal();
    }

    @Override
    public String toString() {
        return this.token.literal();
    }

}
