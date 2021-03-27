package com.monkeyj.lexer;

import com.monkeyj.token.Keywords;
import com.monkeyj.token.Token;

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

    public Token nextToken() {
        this.skipWhitespaces();

        Token tok;

        switch (this.ch) {
            case '=':
                if (this.peekChar() == '=') {
                    final var current = this.ch;
                    this.readChar();
                    tok = new Token(EQ, this.ch + "" + current);
                } else {
                    tok = new Token(ASSIGN, this.ch + "");
                }
                break;
            case ';':
                tok = new Token(SEMICOLON, this.ch + "");
                break;
            case '(':
                tok = new Token(LPAREN, this.ch + "");
                break;
            case ')':
                tok = new Token(RPAREN, this.ch + "");
                break;
            case ',':
                tok = new Token(COMMA, this.ch + "");
                break;
            case '+':
                tok = new Token(PLUS, this.ch + "");
                break;
            case '-':
                tok = new Token(MINUS, this.ch + "");
                break;
            case '{':
                tok = new Token(LBRACE, this.ch + "");
                break;
            case '}':
                tok = new Token(RBRACE, this.ch + "");
                break;
            case '!':
                if (this.peekChar() == '=') {
                    final var current = this.ch;
                    this.readChar();
                    tok = new Token(NOT_EQ, current + "" + this.ch);
                } else {
                     tok = new Token(BANG, this.ch + "");
                }
                break;
            case '/':
                tok = new Token(SLASH, this.ch + "");
                break;
            case '*':
                tok = new Token(ASTERISK, this.ch + "");
                break;
            case '<':
                tok = new Token(LT, this.ch + "");
                break;
            case '>':
                tok = new Token(GT, this.ch + "");
                break;
            case Character.MIN_VALUE:
                tok = new Token(EOF, "");
                break;
            default:
                if (isLetter(this.ch)) {
                    final String literal = this.readIdentifier();
                    tok = new Token(Keywords.lookupIdentifier(literal), literal);
                    return tok;
                } else if (isDigit(this.ch)) {
                    tok = new Token(INT, this.readNumber());
                    return tok;
                } else {
                    tok = new Token(ILLEGAL, this.ch + "");
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