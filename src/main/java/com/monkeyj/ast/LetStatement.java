package com.monkeyj.ast;

import com.monkeyj.token.Token;

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

}
