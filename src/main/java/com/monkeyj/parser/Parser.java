package com.monkeyj.parser;

import com.monkeyj.ast.Identifier;
import com.monkeyj.ast.LetStatement;
import com.monkeyj.ast.Program;
import com.monkeyj.ast.Statement;
import com.monkeyj.lexer.Lexer;
import com.monkeyj.token.Token;
import com.monkeyj.token.TokenConstants;

public class Parser {
    private Lexer lexer;
    private Token curToken;
    private Token peekToken;

    public Parser(final Lexer lexer) {
        this.lexer = lexer;

        this.nextToken();
        this.nextToken();
    }

    public void nextToken() {
        this.curToken = this.peekToken;
        this.peekToken = this.lexer.nextToken();
    }

    public Program parseProgram() {
        final var program = new Program();

        while (!this.curToken.type().equals(TokenConstants.EOF)) {
            final var stmt = this.parseStatement();
            if (stmt != null) {
                program.addStatement(stmt);
            }
            this.nextToken();
        }

        return program;
    }

    private Statement parseStatement() {
        switch (this.curToken.type()) {
            case TokenConstants.LET:
                return this.parseLetStatement();
            default:
                return null;
        }
    }

    private LetStatement parseLetStatement() {
        final var letStmt = new LetStatement();
        letStmt.setToken(this.curToken);

        if (!this.expectPeek(TokenConstants.IDENT)) {
            return null;
        }

        final Identifier identifier = new Identifier(this.curToken, this.curToken.literal());
        letStmt.setName(identifier);

        if (!this.expectPeek(TokenConstants.ASSIGN)) {
            return null;
        }

        // TODO: We're skipping the expression until we encounter a semicolon.
        while (!this.curTokenIs(TokenConstants.SEMICOLON)) {
            this.nextToken();
        }

        return letStmt;
    }

    private boolean expectPeek(final String tokenType) {
        if (this.peekTokenIs(tokenType)) {
            this.nextToken();
            return true;
        }
        return false;
    }

    private boolean peekTokenIs(final String tokenType) {
        return this.peekToken.type().equals(tokenType);
    }

    private boolean curTokenIs(final String tokenType) {
        return this.curToken.type().equals(tokenType);
    }


}
