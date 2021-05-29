package com.monkeyj.lexer;

import com.monkeyj.token.Keywords;
import com.monkeyj.token.Token;

import com.monkeyj.token.TokenConstants;

public class Lexer {

    private final String input;
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
                    tok = new Token(TokenConstants.EQ, this.ch + "" + current);
                } else {
                    tok = new Token(TokenConstants.ASSIGN, this.ch + "");
                }
                break;
            case ';':
                tok = new Token(TokenConstants.SEMICOLON, this.ch + "");
                break;
            case ':':
                tok = new Token(TokenConstants.COLON, this.ch + "");
                break;
            case '(':
                tok = new Token(TokenConstants.LPAREN, this.ch + "");
                break;
            case ')':
                tok = new Token(TokenConstants.RPAREN, this.ch + "");
                break;
            case ',':
                tok = new Token(TokenConstants.COMMA, this.ch + "");
                break;
            case '+':
                tok = new Token(TokenConstants.PLUS, this.ch + "");
                break;
            case '-':
                tok = new Token(TokenConstants.MINUS, this.ch + "");
                break;
            case '{':
                tok = new Token(TokenConstants.LBRACE, this.ch + "");
                break;
            case '}':
                tok = new Token(TokenConstants.RBRACE, this.ch + "");
                break;
            case '!':
                if (this.peekChar() == '=') {
                    final var current = this.ch;
                    this.readChar();
                    tok = new Token(TokenConstants.NOT_EQ, current + "" + this.ch);
                } else {
                     tok = new Token(TokenConstants.BANG, this.ch + "");
                }
                break;
            case '/':
                tok = new Token(TokenConstants.SLASH, this.ch + "");
                break;
            case '*':
                tok = new Token(TokenConstants.ASTERISK, this.ch + "");
                break;
            case '<':
                tok = new Token(TokenConstants.LT, this.ch + "");
                break;
            case '>':
                tok = new Token(TokenConstants.GT, this.ch + "");
                break;
            case Character.MIN_VALUE:
                tok = new Token(TokenConstants.EOF, "");
                break;
            case '"':
                tok = new Token(TokenConstants.STRING, this.readString());
                break;
            case '[':
                tok = new Token(TokenConstants.LBRACKET, this.ch + "");
                break;
            case ']':
                tok = new Token(TokenConstants.RBRACKET, this.ch + "");
                break;
            default:
                if (isLetter(this.ch)) {
                    final String literal = this.readIdentifier();
                    tok = new Token(Keywords.lookupIdentifier(literal), literal);
                    return tok;
                } else if (isDigit(this.ch)) {
                    tok = new Token(TokenConstants.INT, this.readNumber());
                    return tok;
                } else {
                    tok = new Token(TokenConstants.ILLEGAL, this.ch + "");
                }
        }

        this.readChar();

        return tok;
    }

    private String readString() {
        final int pos = this.position + 1;
        do {
            this.readChar();
        } while (this.ch != '"' && this.ch != 0);

        return this.input.substring(pos, this.position);
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

        return this.input.substring(pos, this.position);
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