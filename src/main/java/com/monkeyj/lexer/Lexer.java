package com.monkeyj.lexer;

public class Lexer {

    private String input;
    private int position;               // current position in input (points to the current char).
    private int readPosition;           // current reading position in input (after current char).
    private byte ch;                    // current char under examination.

    public Lexer(final String input) {
        this.input = input;
    }

}
