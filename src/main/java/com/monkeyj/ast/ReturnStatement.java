package com.monkeyj.ast;

import com.monkeyj.token.Token;

public class ReturnStatement implements Statement {

    private Token token;
    private Expression returnValue;

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

    public Expression getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(final Expression returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public String toString() {
        final StringBuilder out = new StringBuilder();

        out.append(this.tokenLiteral()).append(" ");

        if (this.returnValue != null) {
            out.append(this.returnValue.toString());
        }

        out.append(";");

        return out.toString();
    }
}
