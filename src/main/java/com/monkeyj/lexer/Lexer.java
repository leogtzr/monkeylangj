package com.monkeyj.lexer;

import com.monkeyj.token.Keywords;
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
        this.position = this.readPosition;
        this.readPosition++;
    }

    private char peekChar() {
        if (this.readPosition >= this.input.length()) {
            return 0;
        } else {
            return this.input.charAt(this.readPosition);
        }
    }

    public TokenType nextToken() {
        this.skipWhitespaces();

        TokenType tok;

        switch (this.ch) {
            case '=':
                if (this.peekChar() == '=') {
                    final var current = this.ch;
                    this.readChar();
                    tok = new TokenType(EQ, this.ch + "" + current);
                } else {
                    tok = new TokenType(ASSIGN, this.ch + "");
                }
                break;
            case ';':
                tok = new TokenType(SEMICOLON, this.ch + "");
                break;
            case '(':
                tok = new TokenType(LPAREN, this.ch + "");
                break;
            case ')':
                tok = new TokenType(RPAREN, this.ch + "");
                break;
            case ',':
                tok = new TokenType(COMMA, this.ch + "");
                break;
            case '+':
                tok = new TokenType(PLUS, this.ch + "");
                break;
            case '-':
                tok = new TokenType(MINUS, this.ch + "");
                break;
            case '{':
                tok = new TokenType(LBRACE, this.ch + "");
                break;
            case '}':
                tok = new TokenType(RBRACE, this.ch + "");
                break;
            case '!':
                if (this.peekChar() == '=') {
                    final var current = this.ch;
                    this.readChar();
                    tok = new TokenType(NOT_EQ, current + "" + this.ch);
                } else {
                     tok = new TokenType(BANG, this.ch + "");
                }
                break;
            case '/':
                tok = new TokenType(SLASH, this.ch + "");
                break;
            case '*':
                tok = new TokenType(ASTERISK, this.ch + "");
                break;
            case '<':
                tok = new TokenType(LT, this.ch + "");
                break;
            case '>':
                tok = new TokenType(GT, this.ch + "");
                break;
            case Character.MIN_VALUE:
                tok = new TokenType(EOF, "");
                break;
            default:
                if (isLetter(this.ch)) {
                    final String literal = this.readIdentifier();
                    tok = new TokenType(Keywords.lookupIdentifier(literal), literal);
                    return tok;
                } else if (isDigit(this.ch)) {
                    tok = new TokenType(INT, this.readNumber());
                    return tok;
                } else {
                    tok = new TokenType(ILLEGAL, this.ch + "");
                }
        };

        this.readChar();

        return tok;
    }

    private String readNumber() {
        final int pos = this.position;
        while (this.isDigit(this.ch)) {
            this.readChar();
        }

        return this.input.substring(pos, this.position);
    }

    private boolean isDigit(final char ch) {
        return '0' <= ch && ch <= '9';
    }

    private String readIdentifier() {
        final int pos = this.position;
        while (this.isLetter(this.ch)) {
            this.readChar();
        }

        final String ident = this.input.substring(pos, this.position);
        return ident;
    }

    private void skipWhitespaces() {
        while (this.ch == ' ' || this.ch == '\t' || this.ch == '\n' || this.ch == '\r') {
            this.readChar();
        }
    }

    private boolean isLetter(final char ch) {
        return 'a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z' || ch == '_';
    }

}