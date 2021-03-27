package com.monkeyj.parser;

import com.monkeyj.ast.Program;
import com.monkeyj.lexer.Lexer;
import com.monkeyj.token.Token;

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
        // TODO:
        return null;
    }

}
