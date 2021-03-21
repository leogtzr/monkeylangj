package com.monkeyj.token;

public class TokenConstants {

    private TokenConstants() {}

    public static final String ILLEGAL = "ILLEGAL";
    public static final String EOF     = "EOF";

    // Identifiers + literals.
    public static final String IDENT = "IDENT";
    public static final String INT   = "INT";

    // Operators.
    public static final String ASSIGN = "=";
    public static final String PLUS   = "+";

    // Delimiters.
    public static final String COMMA     = ",";
    public static final String SEMICOLON = ";";
    public static final String LPAREN = "(";
    public static final String RPAREN = ")";
    public static final String LBRACE = "{";
    public static final String RBRACE = "}";
    public static final String FUNCTION = "FUNCTION";
    public static final String LET      = "LET";

}
