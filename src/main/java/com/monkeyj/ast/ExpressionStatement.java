package com.monkeyj.ast;

import com.monkeyj.token.Token;

public class ExpressionStatement implements Statement {
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
        return "ExpressionStatement{" +
                "token=" + token +
                ", returnValue=" + returnValue +
                '}';
    }
}
