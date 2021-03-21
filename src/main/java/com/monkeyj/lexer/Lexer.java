package com.monkeyj.lexer;

import com.monkeyj.token.TokenConstants;
import com.monkeyj.token.TokenType;

import static com.monkeyj.token.TokenConstants.*;

public class Lexer {

    private String input;
    private int position;               // current position in input (points to the current char).
    private int readPosition;           // current reading position in input (after current char).
    private char ch;                    // current char under examination.

    public Lexer(final String input) {
        this.input = input;
        this.readChar();
    }

    private void readChar() {
        if (this.readPosition >= this.input.length()) {
            this.ch = 0;
        } else {
            this.ch = this.input.charAt(this.readPosition);
        }
        this.readPosition++;
    }

    public TokenType nextToken() {
        final var tok = switch (this.ch) {
            case '=' -> new TokenType(ASSIGN, this.ch + "");
            case ';' -> new TokenType(SEMICOLON, this.ch + "");
            case '(' -> new TokenType(LPAREN, this.ch + "");
            case ')' -> new TokenType(RPAREN, this.ch + "");
            case ',' -> new TokenType(COMMA, this.ch + "");
            case '+' -> new TokenType(PLUS, this.ch + "");
            case '{' -> new TokenType(LBRACE, this.ch + "");
            case '}' -> new TokenType(RBRACE, this.ch + "");
            case Character.MIN_VALUE -> new TokenType(EOF, "");
            default -> throw new IllegalArgumentException(String.format("Not expected -> [%s]", this.ch));
        };

        this.readChar();
        return tok;
    }

}
