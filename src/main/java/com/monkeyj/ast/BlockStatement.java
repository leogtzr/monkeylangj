package com.monkeyj.ast;

import com.monkeyj.token.Token;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement implements Statement, Node {

    private Token token;
    private List<Statement> statements = new ArrayList<>();

    public BlockStatement() {}

    public BlockStatement(final Token token, final List<Statement> statements) {
        this.token = token;
        this.statements = statements;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(final Token token) {
        this.token = token;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public void setStatements(final List<Statement> statements) {
        this.statements = statements;
    }

    public void addStatement(final Statement stmt) {
        this.statements.add(stmt);
    }

    @Override
    public void statementNode() {}

    @Override
    public String tokenLiteral() {
        return this.token.literal();
    }

    @Override
    public String toString() {
        final var out = new StringBuilder();

        for (final var stmt : this.statements) {
            out.append(stmt.toString());
        }

        return out.toString();
    }
}
